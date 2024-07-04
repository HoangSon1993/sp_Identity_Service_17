package sondev.indentityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sondev.indentityservice.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
