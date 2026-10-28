
package com.scaler.bookMyShow.Repository;

import com.scaler.bookMyShow.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser_Id(int userId);
}
