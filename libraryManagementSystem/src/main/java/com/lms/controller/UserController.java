package com.lms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lms.pojo.User;
import com.lms.service.UserService;
import com.lms.serviceImpl.UserServiceImpl;

@WebServlet("/UserController")
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public UserController() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        System.out.println("UserController action: " + action);

        // =====================================================
        // SHOW ADD USER
        // =====================================================

        if ("showAddUser".equalsIgnoreCase(action)) {

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("jsp/addUser.jsp");

            dispatcher.forward(request, response);
        }

        // =====================================================
        // ADD USER
        // =====================================================

        else if ("addUser".equalsIgnoreCase(action)) {

            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phoneNo = request.getParameter("phone");
            String address = request.getParameter("address");

            User user = new User();

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhoneNo(phoneNo);
            user.setAddress(address);
            user.setRole("USER");

            String randomPass =
                    UUID.randomUUID()
                       .toString()
                       .replace("-", "")
                       .substring(0, 8);

            user.setPassword(randomPass);
            user.setCreatedAt(new Date());

            UserService userService = new UserServiceImpl();

            boolean addFlag = userService.addUser(user);

            if (addFlag) {

                List<User> userList =
                        userService.getAllUserList();

                if (userList != null && userList.size() > 0) {

                    request.setAttribute("userList", userList);

                    RequestDispatcher dispatcher =
                            request.getRequestDispatcher("jsp/userList.jsp");

                    dispatcher.forward(request, response);

                } else {

                    request.setAttribute(
                            "errorMessage",
                            "User created but user list is empty."
                    );

                    RequestDispatcher dispatcher =
                            request.getRequestDispatcher("jsp/addUser.jsp");

                    dispatcher.forward(request, response);
                }

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to create user."
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/addUser.jsp");

                dispatcher.forward(request, response);
            }
        }

        // =====================================================
        // SHOW ADD ADMIN
        // =====================================================

        else if ("showAddAdmin".equalsIgnoreCase(action)) {

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("jsp/addAdmin.jsp");

            dispatcher.forward(request, response);
        }

        // =====================================================
        // ADD ADMIN
        // =====================================================

        else if ("addAdmin".equalsIgnoreCase(action)) {

            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword =
                    request.getParameter("confirmPassword");

            // IMPORTANT:
            // phone_no is NOT NULL in database
            String phoneNo = request.getParameter("phone");

            // -------------------------------------------------
            // Password confirmation
            // -------------------------------------------------

            if (password == null ||
                confirmPassword == null ||
                !password.equals(confirmPassword)) {

                request.setAttribute(
                        "errorMessage",
                        "Passwords do not match."
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/addAdmin.jsp");

                dispatcher.forward(request, response);

                return;
            }

            // -------------------------------------------------
            // Check phone number
            // -------------------------------------------------

            if (phoneNo == null || phoneNo.trim().isEmpty()) {

                request.setAttribute(
                        "errorMessage",
                        "Phone number is required."
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/addAdmin.jsp");

                dispatcher.forward(request, response);

                return;
            }

            // -------------------------------------------------
            // Create Admin object
            // -------------------------------------------------

            User admin = new User();

            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setRole("ADMIN");

            // IMPORTANT FIX
            admin.setPhoneNo(phoneNo);

            // Address can remain NULL
            admin.setAddress(null);

            admin.setCreatedAt(new Date());

            // -------------------------------------------------
            // Save Admin
            // -------------------------------------------------

            UserService userService =
                    new UserServiceImpl();

            boolean addFlag =
                    userService.addUser(admin);

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            if (addFlag) {

                request.setAttribute(
                        "successMessage",
                        "Admin created successfully. You can now login."
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/login.jsp");

                dispatcher.forward(request, response);

            }

            // -------------------------------------------------
            // FAILED
            // -------------------------------------------------

            else {

                request.setAttribute(
                        "errorMessage",
                        "Unable to create admin. Please check the entered details."
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/addAdmin.jsp");

                dispatcher.forward(request, response);
            }
        }

        // =====================================================
        // ALL USERS
        // =====================================================

        else if ("allUserList".equalsIgnoreCase(action)) {

            List<User> userList = new ArrayList<User>();

            UserService userService =
                    new UserServiceImpl();

            userList = userService.getAllUserList();

            if (userList != null && userList.size() > 0) {

                request.setAttribute("userList", userList);

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/userList.jsp");

                dispatcher.forward(request, response);
            }
        }

        // =====================================================
        // VIEW USER
        // =====================================================

        else if ("viewUser".equalsIgnoreCase(action)) {

            String userId =
                    request.getParameter("userId");

            UserService userService =
                    new UserServiceImpl();

            User user =
                    userService.getUserById(
                            Long.parseLong(userId)
                    );

            if (user != null) {

                request.setAttribute("user", user);

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/editUser.jsp");

                dispatcher.forward(request, response);

            } else {

                List<User> userList =
                        userService.getAllUserList();

                request.setAttribute(
                        "errorMessage",
                        "User not found!"
                );

                request.setAttribute(
                        "userList",
                        userList
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/userList.jsp");

                dispatcher.forward(request, response);
            }
        }

        // =====================================================
        // DELETE USER
        // =====================================================

        else if ("deleteUser".equalsIgnoreCase(action)) {

            long userId =
                    Long.parseLong(
                            request.getParameter("userId")
                    );

            UserService service =
                    new UserServiceImpl();

            boolean flag =
                    service.deleteUser(userId);

            if (flag) {

                response.sendRedirect(
                        "UserController?action=allUserList"
                );

            } else {

                request.setAttribute(
                        "errorMessage",
                        "Delete failed"
                );

                request.getRequestDispatcher(
                        "jsp/userList.jsp"
                ).forward(request, response);
            }
        }

        // =====================================================
        // UPDATE USER
        // =====================================================

        else if ("updateUser".equalsIgnoreCase(action)) {

            String firstName =
                    request.getParameter("firstName");

            String lastName =
                    request.getParameter("lastName");

            String phoneNo =
                    request.getParameter("phone");

            String address =
                    request.getParameter("address");

            String email =
                    request.getParameter("email");

            long userId =
                    Long.parseLong(
                            request.getParameter("userId")
                    );

            User user = new User();

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNo(phoneNo);
            user.setAddress(address);
            user.setEmail(email);
            user.setUserId(userId);

            UserService userService =
                    new UserServiceImpl();

            boolean updateFlag =
                    userService.updateUser(user);

            if (updateFlag) {

                request.setAttribute(
                        "user",
                        user
                );

                request.setAttribute(
                        "successMessage",
                        "User updated successfully!"
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/editUser.jsp");

                dispatcher.forward(request, response);

            } else {

                request.setAttribute(
                        "user",
                        user
                );

                request.setAttribute(
                        "errorMessage",
                        "Something went wrong"
                );

                RequestDispatcher dispatcher =
                        request.getRequestDispatcher("jsp/editUser.jsp");

                dispatcher.forward(request, response);
            }
        }
    }

    // =====================================================
    // POST
    // =====================================================

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}