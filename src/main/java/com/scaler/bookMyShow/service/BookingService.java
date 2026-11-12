
package com.scaler.bookMyShow.service;

import com.scaler.bookMyShow.Repository.BookingRepository;
import com.scaler.bookMyShow.Repository.ShowRepository;
import com.scaler.bookMyShow.Repository.ShowSeatRepo;
import com.scaler.bookMyShow.Repository.UserRepository;
import com.scaler.bookMyShow.model.Booking;
import com.scaler.bookMyShow.model.Show;
import com.scaler.bookMyShow.model.ShowSeat;
import com.scaler.bookMyShow.model.User;
import com.scaler.bookMyShow.model.enums.ShowSeatStatus;
import com.scaler.bookMyShow.model.enums.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepo showSeatRepo;
    private final BookingRepository bookingRepository;

    public BookingService(
            UserRepository userRepository,
            ShowRepository showRepository,
            ShowSeatRepo showSeatRepo,
            BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepo = showSeatRepo;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking bookTicket(
            int showId,
            int userId,
            List<Integer> showSeatIds) {

        if (showSeatIds == null || showSeatIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one seat must be selected.");
        }

        if (new HashSet<>(showSeatIds).size() != showSeatIds.size()) {
            throw new IllegalArgumentException(
                    "Duplicate seat IDs are not allowed.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid user ID: " + userId));

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid show ID: " + showId));

        // Acquire pessimistic write locks on all requested seats.
        // The repository query orders them by ID.
        List<ShowSeat> requestedSeats =
                showSeatRepo.findAllByIdInWithLock(showSeatIds);

        if (requestedSeats.size() != showSeatIds.size()) {
            throw new IllegalArgumentException(
                    "One or more requested seats do not exist.");
        }

        // Validate every locked seat before changing any seat.
        for (ShowSeat seat : requestedSeats) {

            if (seat.getShow() == null
                    || seat.getShow().getId() != showId) {
                throw new IllegalArgumentException(
                        "Seat does not belong to the requested show: "
                                + seat.getId());
            }

            if (seat.getStatus() != ShowSeatStatus.AVAILABLE) {
                throw new IllegalStateException(
                        "A requested seat is unavailable: "
                                + seat.getId());
            }
        }

        Date now = new Date();

        // Block all seats after validation succeeds.
        for (ShowSeat seat : requestedSeats) {
            seat.setStatus(ShowSeatStatus.BLOCKED);
            seat.setBlockedAt(now);
        }

        showSeatRepo.saveAll(requestedSeats);

        // Create pending booking.
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setShowSeats(requestedSeats);
        booking.setBookingDate(now);
        booking.setPayments(new ArrayList<>());
        booking.setAmount(2000);
        booking.setTicketStatus(TicketStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForUser(int userId) {
        return bookingRepository.findByUser_Id(userId);
    }
}
