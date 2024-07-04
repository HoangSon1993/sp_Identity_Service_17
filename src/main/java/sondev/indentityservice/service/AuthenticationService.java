package sondev.indentityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sondev.indentityservice.dto.request.AuthenticationRequest;
import sondev.indentityservice.dto.request.IntrospectRequest;
import sondev.indentityservice.dto.response.AuthenticationResponse;
import sondev.indentityservice.dto.response.IntrospectResponse;
import sondev.indentityservice.entity.User;
import sondev.indentityservice.exception.AppException;
import sondev.indentityservice.exception.ErrorCode;
import sondev.indentityservice.repository.UserRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;

import static com.nimbusds.jose.JOSEObjectType.JWT;
import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}") // Đọc biến từ file application.yaml

    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate (AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generatorToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspectResponse (IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

       var veryfied = signedJWT.verify(jwsVerifier);

       return IntrospectResponse.builder()
               .valid(veryfied && expiryTime.after(new Date()))
               .build();
    }

    private String generatorToken(User user){
        // Tạo JWSHeader với JWSAlgorithm.HS512 và thêm thuộc tính "typ": "JWT"
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512) // có thể sử dụng HS256
                .type(JWT) // Thêm (Type: JWT)
                .build();

        // Tạo JWTClaimsSet
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("devteria.com") // Domain
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // Hết hạn sau 1h
                .claim("scope",buildScope(user)) // custom ClaimSet
                .build();

        // Tạo Payload từ JWTClaimsSet
        Payload payload  = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo JWSObject với header và payload
        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            // Ký JWSObject bằng SIGNER_KEY
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // Trả về token đã được ký và serialize
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token: ",e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope (User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
//            user.getRoles().forEach(s -> stringJoiner.add(s));
            //user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}