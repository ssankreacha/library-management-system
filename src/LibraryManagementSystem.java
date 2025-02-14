import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Book class represents a single book in the library.
 * It contains details like ID, title, author, category, year, and borrow status.
 */
class Book {
    String id, title, author, category;
    int year;
    boolean isBorrowed;
    LocalDate dueDate; // Stores the due date if the book is borrowed

    // Constructor to initialize a book
    public Book(String id, String title, String author, String category, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.isBorrowed = false;
        this.dueDate = null;
    }

    // Method to borrow a book
    public void borrowBook() {
        if (!isBorrowed) {
            isBorrowed = true;
            dueDate = LocalDate.now().plusDays(14); // Set return date to 14 days from today
            System.out.println("Book borrowed successfully! Due date: " + dueDate);
        } else {
            System.out.println("Book is already borrowed.");
        }
    }

    // Method to return a book
    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            dueDate = null;
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("This book was not borrowed.");
        }
    }

    // Method to display book details
    @Override
    public String toString() {
        return "ID: " + id + " | " + title + " by " + author + " | " + category + " | Year: " + year +
                (isBorrowed ? " | Borrowed (Due: " + dueDate + ")" : " | Available");
    }
}

/**
 * Library class manages all books, including adding, viewing, searching, borrowing, and returning books.
 */
class Library {
    List<Book> books = new ArrayList<>();
    String dataFile = "library_books.txt"; // File to store book data

    // Constructor - Loads existing books from file
    public Library() {
        loadBooksFromFile();
    }

    // Method to add a book to the library
    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile(); // Save updated list to file
        System.out.println("Book added successfully!");
    }

    // Method to view all books
    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.forEach(System.out::println);
        }
    }

    // Method to search for a book by title or author
    public void searchBook(String keyword) {
        for (Book book : books) {
            if (book.title.toLowerCase().contains(keyword.toLowerCase()) ||
                    book.author.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(book);
            }
        }
    }

    // Method to borrow a book by ID
    public void borrowBook(String id) {
        for (Book book : books) {
            if (book.id.equals(id)) {
                book.borrowBook();
                saveBooksToFile(); // Save changes to file
                return;
            }
        }
        System.out.println("Book not found.");
    }

    // Method to return a borrowed book by ID
    public void returnBook(String id) {
        for (Book book : books) {
            if (book.id.equals(id)) {
                book.returnBook();
                saveBooksToFile(); // Save changes to file
                return;
            }
        }
        System.out.println("Book not found.");
    }

    // Save all books to a file for persistence
    public void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            for (Book book : books) {
                writer.println(book.id + "," + book.title + "," + book.author + "," + book.category + "," +
                        book.year + "," + book.isBorrowed + "," + (book.dueDate != null ? book.dueDate : "null"));
            }
        } catch (IOException e) {
            System.out.println("Error saving books.");
        }
    }

    // Load books from file when program starts
    public void loadBooksFromFile() {
        File file = new File(dataFile);
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                Book book = new Book(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]));
                book.isBorrowed = Boolean.parseBoolean(data[5]);
                book.dueDate = data[6].equals("null") ? null : LocalDate.parse(data[6]);
                books.add(book);
            }
        } catch (IOException e) {
            System.out.println("Error loading books.");
        }
    }
}

/**
 * Main class to run the Library Management System.
 */
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            // Display menu options
            System.out.println("\n📚 Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Books");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Adding a book
                    System.out.print("Enter Book ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter Year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    library.addBook(new Book(id, title, author, category, year));
                    break;
                case 2:
                    library.viewBooks();
                    break;
                case 3:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    library.searchBook(keyword);
                    break;
                case 4:
                    System.out.print("Enter Book ID to borrow: ");
                    String borrowId = scanner.nextLine();
                    library.borrowBook(borrowId);
                    break;
                case 5:
                    System.out.print("Enter Book ID to return: ");
                    String returnId = scanner.nextLine();
                    library.returnBook(returnId);
                    break;
                case 6:
                    System.out.println("Exiting Library Management System...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
