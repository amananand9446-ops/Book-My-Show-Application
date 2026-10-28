
package com.scaler.bookMyShow.controller;

import com.scaler.bookMyShow.Repository.UserRepository;
import com.scaler.bookMyShow.model.Booking;
import com.scaler.bookMyShow.model.User;
import com.scaler.bookMyShow.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(
            BookingService bookingService,
            UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking bookTicket(
            @RequestParam int showId,
            @RequestBody List<Integer> showSeatIds,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Authenticated user not found"));

        return bookingService.bookTicket(
                showId,
                user.getId(),
                showSeatIds
        );
    }

    @GetMapping
    public List<Booking> getMyBookings(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Authenticated user not found"));

        return bookingService.getBookingsForUser(user.getId());
    }
}
