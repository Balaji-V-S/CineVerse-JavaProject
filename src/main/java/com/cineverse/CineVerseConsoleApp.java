package com.cineverse;

import com.cineverse.entity.*;
import com.cineverse.service.*;
import com.cineverse.util.JPAUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CineVerseConsoleApp {

    private static final UserService userService = new UserService();
    private static final MovieService movieService = new MovieService();
    private static final IBookable bookingService = new BookingService();
    private static final ReviewService reviewService = new ReviewService();
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        // Create a default admin if it doesn't exist
        initializeAdmin();

        while (true) {
            showMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    if (loggedInUser != null) {
                        showLoggedInMenu();
                    } else {
                        System.out.println("Please log in first.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting CineVerse. Goodbye!");
                    JPAUtil.shutdown();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n--- Welcome to CineVerse ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Go to Main Menu (if logged in)");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void showLoggedInMenu() {
        while (loggedInUser != null) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("Logged in as: " + loggedInUser.getName() + " (" + (loggedInUser instanceof Admin ? "Admin" : "Customer") + ")");
            System.out.println("1. View All Movies");
            System.out.println("2. Book Tickets");
            System.out.println("3. Add a Review");
            if (loggedInUser instanceof Admin) {
                System.out.println("4. Add New Movie (Admin)");
            }
            System.out.println("9. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    bookTickets();
                    break;
                case 3:
                    addReview();
                    break;
                case 4:
                    if (loggedInUser instanceof Admin) {
                        addMovie();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 9:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initializeAdmin() {
        try {
            userService.registerUser("Admin", "admin@cineverse.com", "admin123", true);
            System.out.println("Default admin user 'admin@cineverse.com' created/verified.");
        } catch (com.cineverse.exception.UserAlreadyExistsException e) {
            // This is expected if the admin already exists.
            System.out.println("Default admin user 'admin@cineverse.com' already exists.");
        } catch (Exception e) {
            System.err.println("Error initializing admin: " + e.getMessage());
        }
    }

    private static void registerUser() {
        System.out.println("\n--- Register New User ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            userService.registerUser(name, email, password, false);
            System.out.println("Registration successful! Please log in.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void login() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (jakarta.persistence.EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (user.getPassword().equals(password)) {
                loggedInUser = user;
                System.out.println("Login successful! Welcome, " + user.getName());
                showLoggedInMenu();
            } else {
                System.out.println("Invalid password.");
            }
        } catch (jakarta.persistence.NoResultException e) {
            System.out.println("No user found with that email.");
        }
    }

    private static void logout() {
        loggedInUser = null;
        System.out.println("You have been logged out.");
    }

    private static void addMovie() {
        try {
            System.out.println("\n--- Add New Movie ---");
            System.out.print("Enter movie title: ");
            String title = scanner.nextLine();
            System.out.print("Enter genre: ");
            String genre = scanner.nextLine();
            System.out.print("Enter duration (minutes): ");
            int duration = getIntInput();
            System.out.print("Enter release date (YYYY-MM-DD): ");
            LocalDate releaseDate = LocalDate.parse(scanner.nextLine());

            List<ShowTime> showTimes = new ArrayList<>();
            System.out.println("Enter showtimes (type 'done' when finished):");
            while(true) {
                System.out.print("Enter showtime (YYYY-MM-DD HH:mm): ");
                String dtStr = scanner.nextLine();
                if(dtStr.equalsIgnoreCase("done")) break;

                try {
                    LocalDateTime startTime = LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    System.out.print("Enter available seats for this showtime: ");
                    int seats = getIntInput();

                    ShowTime st = new ShowTime();
                    st.setStartTime(startTime);
                    st.setAvailableSeats(seats);
                    showTimes.add(st);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date/time format. Please use YYYY-MM-DD HH:mm.");
                }
            }

            movieService.addMovie(loggedInUser, title, genre, duration, releaseDate, showTimes);
            System.out.println("Movie added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding movie: " + e.getMessage());
            // **IMPROVEMENT**: Print the full error details for debugging.
            e.printStackTrace();
        }
    }

    private static void viewAllMovies() {
        System.out.println("\n--- Available Movies ---");
        List<Movie> movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            System.out.println("No movies available at the moment.");
            return;
        }
        for (Movie movie : movies) {
            System.out.printf("ID: %d, Title: %s, Genre: %s, Rating: %.1f%n",
                    movie.getMovieId(), movie.getTitle(), movie.getGenre(), movie.getOverallRating());
            System.out.println("  Showtimes:");
            for (ShowTime st : movie.getShowTimes()) {
                System.out.printf("    - ID: %d, Time: %s, Seats Available: %d%n",
                        st.getShowTimeId(),
                        st.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        st.getAvailableSeats());
            }
        }
    }

    private static void bookTickets() {
        // **BUG FIX**: Prevent admins from accessing customer functions.
        if (loggedInUser instanceof Admin) {
            System.out.println("Admins cannot book tickets.");
            return;
        }
        System.out.println("\n--- Book Tickets ---");
        viewAllMovies();
        System.out.print("Enter the Showtime ID you want to book: ");
        long showTimeId = getLongInput();
        System.out.print("Enter number of seats: ");
        int seats = getIntInput();

        try {
            bookingService.bookTickets(loggedInUser.getUserId(), showTimeId, seats);
            System.out.println("Booking successful!");
        } catch (Exception e) {
            System.out.println("Error booking tickets: " + e.getMessage());
        }
    }

    private static void addReview() {
        // **BUG FIX**: Prevent admins from accessing customer functions.
        if (loggedInUser instanceof Admin) {
            System.out.println("Admins cannot add reviews.");
            return;
        }
        System.out.println("\n--- Add a Review ---");
        List<Movie> movies = movieService.getAllMovies();
        for (Movie movie : movies) {
            System.out.printf("ID: %d, Title: %s%n", movie.getMovieId(), movie.getTitle());
        }
        System.out.print("Enter the Movie ID you want to review: ");
        long movieId = getLongInput();
        System.out.print("Enter rating (1-5): ");
        int rating = getIntInput();
        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();

        try {
            reviewService.addReview(loggedInUser.getUserId(), movieId, comment, rating);
            System.out.println("Thank you for your review!");
        } catch (Exception e) {
            System.out.println("Error submitting review: " + e.getMessage());
        }
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
            }
        }
    }

    private static long getLongInput() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
            }
        }
    }
}

