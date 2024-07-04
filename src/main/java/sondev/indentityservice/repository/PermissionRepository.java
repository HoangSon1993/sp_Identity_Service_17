package sondev.indentityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sondev.indentityservice.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
