//package ch.uzh.ifi.hase.soprafs21.repository;
//
//import ch.uzh.ifi.hase.soprafs21.entity.ProfileImage;
//import ch.uzh.ifi.hase.soprafs21.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.UUID;
//
//@Repository("imageRepository")
//public interface ImageRepository extends JpaRepository<ProfileImage, UUID> {
//    @Query(value = "SELECT u FROM ProfileImage u WHERE u.userId = :userId")
//    ProfileImage findByUserIdIs(@Param("userId") UUID userId);
//}

