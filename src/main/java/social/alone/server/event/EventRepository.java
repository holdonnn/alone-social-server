package social.alone.server.event;

import social.alone.server.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {

  Page<Event> findByOwner(User user, Pageable pageable);

  Page<Event> findByUsersContaining(User user, Pageable pageable);
}