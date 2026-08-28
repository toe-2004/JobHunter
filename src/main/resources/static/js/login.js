document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("loginForm");

    const email = document.getElementById("email");
    const password = document.getElementById("password");

    const emailError = document.getElementById("emailError");
    const passwordError = document.getElementById("passwordError");


    function setError(input, errorElement, message) {

        input.classList.add("is-invalid");
        input.classList.remove("is-valid");

        errorElement.textContent = message;
        errorElement.classList.add("show");
    }


    function clearError(input, errorElement) {

        input.classList.remove("is-invalid");
        input.classList.remove("is-valid");

        errorElement.textContent = "";
        errorElement.classList.remove("show");
    }


    function setValid(input, errorElement) {

        input.classList.remove("is-invalid");
        input.classList.add("is-valid");

        errorElement.textContent = "";
        errorElement.classList.remove("show");
    }


    function validateEmail() {

        const value = email.value.trim();

        if (value === "") {

            setError(
                email,
                emailError,
                "Email is required."
            );

            return false;
        }


        const emailPattern =
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;


        if (!emailPattern.test(value)) {

            setError(
                email,
                emailError,
                "Please enter a valid email address."
            );

            return false;
        }


        setValid(email, emailError);

        return true;
    }


    function validatePassword() {

        const value = password.value;

        if (value.trim() === "") {

            setError(
                password,
                passwordError,
                "Password is required."
            );

            return false;
        }


        if (value.length < 6) {

            setError(
                password,
                passwordError,
                "Password must be at least 6 characters."
            );

            return false;
        }


        setValid(password, passwordError);

        return true;
    }


    email.addEventListener("blur", function () {

        validateEmail();

    });


    password.addEventListener("blur", function () {

        validatePassword();

    });


    email.addEventListener("input", function () {

        if (this.value.trim() === "") {

            clearError(
                email,
                emailError
            );

        } else {

            validateEmail();

        }

    });


    password.addEventListener("input", function () {

        if (this.value.trim() === "") {

            clearError(
                password,
                passwordError
            );

        } else {

            validatePassword();

        }

    });


    form.addEventListener("submit", function (event) {

        const validEmail = validateEmail();
        const validPassword = validatePassword();


        if (!validEmail || !validPassword) {

            event.preventDefault();

            const firstInvalid =
                form.querySelector(".is-invalid");

            if (firstInvalid) {

                firstInvalid.scrollIntoView({
                    behavior: "smooth",
                    block: "center"
                });

                firstInvalid.focus();
            }

            return;
        }

    });

});
