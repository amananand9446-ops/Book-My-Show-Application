
package com.scaler.bookMyShow.Repository;

import com.scaler.bookMyShow.model.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepo extends JpaRepository<ShowSeat, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.id = :seatId")
    Optional<ShowSeat> findByIdForUpdate(@Param("seatId") int seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.id IN :seatIds ORDER BY s.id ASC")
    List<ShowSeat> findAllByIdInWithLock(
            @Param("seatIds") List<Integer> seatIds
    );

    List<ShowSeat> findByShow_Id(int showId);
}
