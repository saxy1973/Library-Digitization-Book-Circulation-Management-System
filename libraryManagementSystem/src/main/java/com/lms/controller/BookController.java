
package com.lms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.lms.pojo.Book;
import com.lms.pojo.BookIssued;
import com.lms.pojo.User;
import com.lms.service.BookService;
import com.lms.service.UserService;
import com.lms.serviceImpl.BookServiceImpl;
import com.lms.serviceImpl.UserServiceImpl;

@WebServlet("/BookController")
public class BookController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null) {
            action = action.trim();
        }

        System.out.println("action: " + action);

        if ("showAddBook".equalsIgnoreCase(action)) {
            request.getRequestDispatcher("jsp/addBook.jsp").forward(request, response);
        }

        else if ("addbook".equalsIgnoreCase(action)) {
            Book book = new Book();
            book.setTitle(request.getParameter("bookTitle"));
            book.setAuthor(request.getParameter("author"));
            book.setCategory(request.getParameter("category"));
            book.setIsbn(request.getParameter("isbn"));
            book.setPublisher(request.getParameter("publisher"));
            book.setTotalCopies(Integer.parseInt(request.getParameter("numberofcopies")));
            book.setAvailableCopies(Integer.parseInt(request.getParameter("availableCopies")));
            book.setCreatedAt(new Date());
            book.setStatus("AVAILABLE");

            BookService service = new BookServiceImpl();
            boolean flag = service.addBook(book);

            if (flag) {
                request.setAttribute("booklist", service.getAllBookList());
                request.setAttribute("successMessage", "Book added successfully!!");
                request.getRequestDispatcher("jsp/bookList.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Something went wrong");
                request.getRequestDispatcher("jsp/addBook.jsp").forward(request, response);
            }
        }

        else if ("allBookList".equalsIgnoreCase(action)) {
            BookService service = new BookServiceImpl();
            request.setAttribute("booklist", service.getAllBookList());
            request.getRequestDispatcher("jsp/bookList.jsp").forward(request, response);
        }

        else if ("viewBook".equalsIgnoreCase(action)) {
            long id = Long.parseLong(request.getParameter("bookId"));
            Book book = new BookServiceImpl().getBookById(id);

            if (book != null) {
                request.setAttribute("book", book);
                request.getRequestDispatcher("jsp/editBook.jsp").forward(request, response);
            }
        }

        else if ("updateBook".equalsIgnoreCase(action)) {
            Book book = new Book();
            book.setBookId(Long.parseLong(request.getParameter("bookId")));
            book.setTitle(request.getParameter("bookTitle"));
            book.setAuthor(request.getParameter("author"));
            book.setCategory(request.getParameter("category"));
            book.setIsbn(request.getParameter("isbn"));
            book.setPublisher(request.getParameter("publisher"));
            book.setTotalCopies(Integer.parseInt(request.getParameter("numberofcopies")));
            book.setAvailableCopies(Integer.parseInt(request.getParameter("availableCopies")));

            boolean updated = new BookServiceImpl().updateBook(book);

            request.setAttribute("book", book);
            request.setAttribute(updated ? "successMessage" : "errorMessage",
                    updated ? "Updated successfully" : "Update failed");

            request.getRequestDispatcher("jsp/editBook.jsp").forward(request, response);
        }

        else if ("showAssignBook".equalsIgnoreCase(action)) {
            BookService b = new BookServiceImpl();
            UserService u = new UserServiceImpl();

            request.setAttribute("bookList", b.getAllAvailableBookList());
            request.setAttribute("userList", u.getAllUserList());

            request.getRequestDispatcher("jsp/assignBook.jsp").forward(request, response);
        }

        else if ("assignBook".equalsIgnoreCase(action)) {
            long bookId = Long.parseLong(request.getParameter("bookId"));
            long userId = Long.parseLong(request.getParameter("userId"));

            LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));

            BookIssued issued = new BookIssued();
            issued.setBook(new Book());
            issued.getBook().setBookId(bookId);

            issued.setUser(new User());
            issued.getUser().setUserId(userId);

            issued.setDueDate(dueDate);

            boolean success = new BookServiceImpl().assignBook(issued);

            if (success) {
                response.sendRedirect("BookController?action=showAssignBook");
            } else {
                request.setAttribute("errorMessage", "Assign failed");
                request.getRequestDispatcher("jsp/assignBook.jsp").forward(request, response);
            }
        }

        // ✅ RETURN LIST
        else if ("showReturnBook".equalsIgnoreCase(action)) {
            BookService service = new BookServiceImpl();
            List<BookIssued> list = service.getAllIssuedBookList();

            if (list != null && list.size() > 0) {
                LocalDate today = LocalDate.now();

                for (BookIssued b : list) {
                    if (b.getDueDate().isBefore(today))
                        b.setDueDayStatus("Overdue");
                    else if (b.getDueDate().isEqual(today))
                        b.setDueDayStatus("Due Today");
                    else
                        b.setDueDayStatus("Active");
                }

                request.setAttribute("issuedList", list);
            } else {
                request.setAttribute("errorMessage", "No issued books found");
            }

            request.getRequestDispatcher("jsp/returnBook.jsp").forward(request, response);
        }

        // ✅ 🔥 NEW BLOCK (IMPORTANT FIX)
        else if ("showReturnBookDetails".equalsIgnoreCase(action)) {

            long issuedId = Long.parseLong(request.getParameter("issuedId"));

            BookService service = new BookServiceImpl();
            BookIssued issued = service.getIssuedBookById(issuedId);

            if (issued != null) {
                request.setAttribute("bookIssued", issued);
            } else {
                request.setAttribute("errorMessage", "Book data not found");
            }

            request.getRequestDispatcher("jsp/showReturnBookDetails.jsp").forward(request, response);
        }

        // ✅ RETURN BOOK
        else if ("returnBook".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("issuedId"));
            LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));

            BookIssued issued = new BookIssued();
            issued.setIssueId(id);
            issued.setReturnDate(returnDate);

            new BookServiceImpl().updateBookReturn(issued);

            response.sendRedirect("BookController?action=showReturnBook");
        }
     // ✅ DELETE BOOK
        else if ("deleteBook".equalsIgnoreCase(action)) {

            long bookId = Long.parseLong(request.getParameter("bookId"));

            BookService service = new BookServiceImpl();
            boolean flag = service.deleteBook(bookId);

            if (flag) {
                response.sendRedirect("BookController?action=allBookList");
            } else {
                request.setAttribute("errorMessage", "Delete failed");
                request.getRequestDispatcher("jsp/bookList.jsp").forward(request, response);
            }
        }

        else {
            response.getWriter().println("Invalid action");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}