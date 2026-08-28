document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("employerForm");

    const companyName = document.getElementById("companyName");
    const companyEmail = document.getElementById("companyEmail");
    const companyPhone = document.getElementById("companyPhone");
    const companyLocation = document.getElementById("companyLocation");
    const companyDescription = document.getElementById("companyDescription");

    const companyNameError = document.getElementById("companyNameError");
    const companyEmailError = document.getElementById("companyEmailError");
    const companyPhoneError = document.getElementById("companyPhoneError");
    const companyLocationError = document.getElementById("companyLocationError");
    const companyDescriptionError =
        document.getElementById("companyDescriptionError");

    const charCount = document.getElementById("charCount");

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

    function setValid(input) {

        input.classList.remove("is-invalid");

        if (input.value.trim() !== "") {
            input.classList.add("is-valid");
        }
    }

    function validateCompanyName() {

        const value = companyName.value.trim();

        if (value === "") {
            setError(
                companyName,
                companyNameError,
                "Company name is required."
            );
            return false;
        }

        if (value.length < 2) {
            setError(
                companyName,
                companyNameError,
                "Company name must be at least 2 characters."
            );
            return false;
        }

        if (!/[A-Za-z\u1000-\u109F]/.test(value)) {
            setError(
                companyName,
                companyNameError,
                "Please enter a valid company name."
            );
            return false;
        }

        clearError(companyName, companyNameError);
        setValid(companyName);

        return true;
    }

    function validateEmail() {

        const value = companyEmail.value.trim();

        if (value === "") {
            setError(
                companyEmail,
                companyEmailError,
                "Company email is required."
            );
            return false;
        }

        const emailPattern =
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        if (!emailPattern.test(value)) {
            setError(
                companyEmail,
                companyEmailError,
                "Please enter a valid email address."
            );
            return false;
        }

        clearError(companyEmail, companyEmailError);
        setValid(companyEmail);

        return true;
    }

    function validatePhone() {

        const value = companyPhone.value.trim();

        if (value === "") {
            setError(
                companyPhone,
                companyPhoneError,
                "Company phone is required."
            );
            return false;
        }

        const cleanPhone = value.replace(/[\s\-()]/g, "");

        const phonePattern =
            /^(09\d{7,9}|\+959\d{7,9})$/;

        if (!phonePattern.test(cleanPhone)) {
            setError(
                companyPhone,
                companyPhoneError,
                "Please enter a valid Myanmar phone number."
            );
            return false;
        }

        clearError(companyPhone, companyPhoneError);
        setValid(companyPhone);

        return true;
    }

    function validateLocation() {

        const value = companyLocation.value.trim();

        if (value === "") {
            setError(
                companyLocation,
                companyLocationError,
                "Company location is required."
            );
            return false;
        }

        if (value.length < 2) {
            setError(
                companyLocation,
                companyLocationError,
                "Please enter a valid location."
            );
            return false;
        }

        clearError(companyLocation, companyLocationError);
        setValid(companyLocation);

        return true;
    }

    function validateDescription() {

        const value = companyDescription.value.trim();

        if (value === "") {
            setError(
                companyDescription,
                companyDescriptionError,
                "Company description is required."
            );
            return false;
        }

        if (value.length < 20) {
            setError(
                companyDescription,
                companyDescriptionError,
                "Company description must be at least 20 characters."
            );
            return false;
        }

        clearError(
            companyDescription,
            companyDescriptionError
        );

        setValid(companyDescription);

        return true;
    }

    function updateCharacterCount() {

        const length = companyDescription.value.length;

        charCount.textContent = length + " / 1000";

        if (length > 950) {
            charCount.classList.add("warning");
        } else {
            charCount.classList.remove("warning");
        }
    }

    companyName.addEventListener("blur", validateCompanyName);

    companyEmail.addEventListener("blur", validateEmail);

    companyPhone.addEventListener("blur", validatePhone);

    companyLocation.addEventListener("blur", validateLocation);

    companyDescription.addEventListener(
        "blur",
        validateDescription
    );

    companyName.addEventListener("input", function () {

        if (this.value.trim() === "") {
            clearError(companyName, companyNameError);
        } else {
            validateCompanyName();
        }

    });

    companyEmail.addEventListener("input", function () {

        if (this.value.trim() === "") {
            clearError(companyEmail, companyEmailError);
        } else {
            validateEmail();
        }

    });

    companyPhone.addEventListener("input", function () {

        this.value = this.value.replace(
            /[^0-9+\-\s()]/g,
            ""
        );

        if (this.value.trim() === "") {
            clearError(companyPhone, companyPhoneError);
        } else {
            validatePhone();
        }

    });

    companyLocation.addEventListener("input", function () {

        if (this.value.trim() === "") {
            clearError(companyLocation, companyLocationError);
        } else {
            validateLocation();
        }

    });

    companyDescription.addEventListener(
        "input",
        function () {

            updateCharacterCount();

            if (this.value.trim() === "") {
                clearError(
                    companyDescription,
                    companyDescriptionError
                );
            } else {
                validateDescription();
            }

        }
    );

    form.addEventListener("submit", function (event) {

        const validName = validateCompanyName();
        const validEmail = validateEmail();
        const validPhone = validatePhone();
        const validLocation = validateLocation();
        const validDescription = validateDescription();

        if (
            !validName ||
            !validEmail ||
            !validPhone ||
            !validLocation ||
            !validDescription
        ) {

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
        }
    });

    updateCharacterCount();

});
