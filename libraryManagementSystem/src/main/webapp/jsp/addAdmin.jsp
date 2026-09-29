<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Add Admin - Library Management System</title>

    <!-- Bootstrap -->
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- Add Admin CSS -->
    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/addAdmin.css">

</head>


<body class="add-admin-page">


<div class="add-admin-container">

    <div class="add-admin-card">


        <!-- ================================================= -->
        <!-- HEADER -->
        <!-- ================================================= -->

        <div class="add-admin-header">

            <div class="add-admin-icon">

                <i class="bi bi-person-plus-fill"></i>

            </div>


            <h1 class="add-admin-title">
                Create Admin Account
            </h1>


            <p class="add-admin-subtitle">
                Create a new administrator account
            </p>

        </div>



        <!-- ================================================= -->
        <!-- ERROR MESSAGE -->
        <!-- ================================================= -->

        <%
            String errorMessage =
                (String) request.getAttribute("errorMessage");

            if (errorMessage != null) {
        %>

            <div class="add-admin-error">

                <i class="bi bi-exclamation-circle me-2"></i>

                <%= errorMessage %>

            </div>

        <%
            }
        %>



        <!-- ================================================= -->
        <!-- FORM -->
        <!-- ================================================= -->

        <form
            action="${pageContext.request.contextPath}/UserController"
            method="post">


            <!-- Hidden Action -->

            <input
                type="hidden"
                name="action"
                value="addAdmin">


            <!-- ================================================= -->
            <!-- FIRST NAME -->
            <!-- ================================================= -->

            <div class="add-admin-form-group">

                <input
                    type="text"
                    name="firstName"
                    class="add-admin-input"
                    placeholder="First Name"
                    autocomplete="given-name"
                    required>

            </div>


            <!-- ================================================= -->
            <!-- LAST NAME -->
            <!-- ================================================= -->

            <div class="add-admin-form-group">

                <input
                    type="text"
                    name="lastName"
                    class="add-admin-input"
                    placeholder="Last Name"
                    autocomplete="family-name"
                    required>

            </div>


            <!-- ================================================= -->
            <!-- EMAIL -->
            <!-- ================================================= -->

            <div class="add-admin-form-group">

                <input
                    type="email"
                    name="email"
                    class="add-admin-input"
                    placeholder="Admin Email"
                    autocomplete="email"
                    required>

            </div>


            <!-- ================================================= -->
            <!-- PHONE NUMBER -->
            <!-- ================================================= -->

            <div class="add-admin-form-group">

                <input
                    type="tel"
                    name="phone"
                    class="add-admin-input"
                    placeholder="Phone Number"
                    autocomplete="tel"
                    pattern="[0-9]{10}"
                    maxlength="10"
                    required>

            </div>


            <!-- ================================================= -->
            <!-- PASSWORD -->
            <!-- ================================================= -->

            <div class="add-admin-form-group">

                <input
                    type="password"
                    name="password"
                    class="add-admin-input"
                    placeholder="Password"
                    autocomplete="new-password"
                    required>

            </div>


            <!-- ================================================= -->
            <!-- CONFIRM PASSWORD -->
            <!-- ================================================= -->

            <div class="add-admin-form-group">

                <input
                    type="password"
                    name="confirmPassword"
                    class="add-admin-input"
                    placeholder="Confirm Password"
                    autocomplete="new-password"
                    required>

            </div>


            <!-- ================================================= -->
            <!-- CREATE ADMIN BUTTON -->
            <!-- ================================================= -->

            <button
                type="submit"
                class="add-admin-button">

                <i class="bi bi-person-plus-fill me-2"></i>

                Create Admin

            </button>


        </form>



        <!-- ================================================= -->
        <!-- DIVIDER -->
        <!-- ================================================= -->

        <div class="add-admin-divider">

            <span>
                Already have an account?
            </span>

        </div>



        <!-- ================================================= -->
        <!-- BACK TO LOGIN -->
        <!-- ================================================= -->

        <a
            href="${pageContext.request.contextPath}/AuthenticationController?action=showLogin"
            class="add-admin-back">

            <i class="bi bi-box-arrow-in-left me-2"></i>

            Back to Login

        </a>


    </div>

</div>


</body>

</html>