document.addEventListener("DOMContentLoaded", function () {

    const form =
        document.getElementById("employerProfileForm");

    const photoInput =
        document.getElementById("profilePhoto");

    const photoContainer =
        document.getElementById("photoContainer");

    const photoPreview =
        document.getElementById("photoPreview");

    const photoPlaceholder =
        document.getElementById("photoPlaceholder");

    const photoError =
        document.getElementById("photoError");

    const selectedFileName =
        document.getElementById("selectedFileName");

    const companyName =
        document.getElementById("companyName");

    const companyEmail =
        document.getElementById("companyEmail");

    const companyPhone =
        document.getElementById("companyPhone");

    const companyLocation =
        document.getElementById("companyLocation");

    const companyDescription =
        document.getElementById("companyDescription");

    const name =
        document.getElementById("name");

    const companyNameError =
        document.getElementById("companyNameError");

    const companyEmailError =
        document.getElementById("companyEmailError");

    const companyPhoneError =
        document.getElementById("companyPhoneError");

    const companyLocationError =
        document.getElementById("companyLocationError");

    const nameError =
        document.getElementById("nameError");

    function setError(input, errorElement, message) {

        if (input) {
            input.classList.add("client-invalid");
        }

        if (errorElement) {
            errorElement.textContent = message;
        }
    }

    function clearError(input, errorElement) {

        if (input) {
            input.classList.remove("client-invalid");
        }

        if (errorElement) {
            errorElement.textContent = "";
        }
    }

    function validateRequired(input, errorElement, message) {

        if (!input) {
            return true;
        }

        const value =
            input.value.trim();

        if (value === "") {

            setError(
                input,
                errorElement,
                message
            );

            return false;
        }

        clearError(
            input,
            errorElement
        );

        return true;
    }

    function validateEmail() {

        if (!companyEmail) {
            return true;
        }

        const value =
            companyEmail.value.trim();

        if (value === "") {

            setError(
                companyEmail,
                companyEmailError,
                "Company email is required."
            );

            return false;
        }

        const emailPattern =
            /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

        if (!emailPattern.test(value)) {

            setError(
                companyEmail,
                companyEmailError,
                "Please enter a valid email address."
            );

            return false;
        }

        clearError(
            companyEmail,
            companyEmailError
        );

        return true;
    }

    function validatePhone() {

        if (!companyPhone) {
            return true;
        }

        const value =
            companyPhone.value.trim();

        if (value === "") {

            setError(
                companyPhone,
                companyPhoneError,
                "Company phone is required."
            );

            return false;
        }

        if (!/^[0-9]+$/.test(value)) {

            setError(
                companyPhone,
                companyPhoneError,
                "Phone number must contain numbers only."
            );

            return false;
        }

        if (value.length < 7 || value.length > 15) {

            setError(
                companyPhone,
                companyPhoneError,
                "Phone number must be between 7 and 15 digits."
            );

            return false;
        }

        clearError(
            companyPhone,
            companyPhoneError
        );

        return true;
    }

    function validatePhoto() {

        if (!photoInput || !photoInput.files.length) {
            return true;
        }

        const file =
            photoInput.files[0];

        const allowedTypes = [
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
        ];

        if (!allowedTypes.includes(file.type)) {

            if (photoError) {
                photoError.textContent =
                    "Please select a JPG, JPEG, PNG or WEBP image.";
            }

            photoInput.value = "";

            if (selectedFileName) {
                selectedFileName.textContent = "";
            }

            return false;
        }

        const maxSize =
            5 * 1024 * 1024;

        if (file.size > maxSize) {

            if (photoError) {
                photoError.textContent =
                    "Image size must be less than 5 MB.";
            }

            photoInput.value = "";

            if (selectedFileName) {
                selectedFileName.textContent = "";
            }

            return false;
        }

        if (photoError) {
            photoError.textContent = "";
        }

        return true;
    }

    if (companyName) {

        companyName.addEventListener(
            "input",
            function () {
                validateRequired(
                    companyName,
                    companyNameError,
                    "Company name is required."
                );
            }
        );

    }

    if (companyEmail) {

        companyEmail.addEventListener(
            "input",
            function () {
                validateEmail();
            }
        );

        companyEmail.addEventListener(
            "blur",
            function () {
                companyEmail.value =
                    companyEmail.value.trim();

                validateEmail();
            }
        );

    }

    if (companyPhone) {

        companyPhone.addEventListener(
            "input",
            function () {

                this.value =
                    this.value.replace(/\D/g, "");

                if (this.value.length > 15) {
                    this.value =
                        this.value.substring(0, 15);
                }

                validatePhone();
            }
        );

        companyPhone.addEventListener(
            "blur",
            function () {

                this.value =
                    this.value.trim();

                validatePhone();
            }
        );

    }

    if (companyLocation) {

        companyLocation.addEventListener(
            "input",
            function () {
                validateRequired(
                    companyLocation,
                    companyLocationError,
                    "Company location is required."
                );
            }
        );

    }

    if (name) {

        name.addEventListener(
            "input",
            function () {
                validateRequired(
                    name,
                    nameError,
                    "Your name is required."
                );
            }
        );

    }

    if (photoInput) {

        photoInput.addEventListener(
            "change",
            function () {

                const file =
                    this.files[0];

                if (!file) {
                    return;
                }

                if (!validatePhoto()) {
                    return;
                }

                if (selectedFileName) {
                    selectedFileName.textContent =
                        file.name;
                }

                const reader =
                    new FileReader();

                reader.onload =
                    function (event) {

                        if (!photoContainer) {
                            return;
                        }

                        let image =
                            document.getElementById(
                                "photoPreview"
                            );

                        if (!image) {

                            image =
                                document.createElement("img");

                            image.id =
                                "photoPreview";

                            image.className =
                                "profile-photo";

                            image.alt =
                                "Company Photo";

                            photoContainer.innerHTML = "";

                            photoContainer.appendChild(
                                image
                            );
                        }

                        image.src =
                            event.target.result;
                    };

                reader.readAsDataURL(file);

            }
        );

    }

    if (form) {

        form.addEventListener(
            "submit",
            function (event) {

                if (companyName) {
                    companyName.value =
                        companyName.value.trim();
                }

                if (companyEmail) {
                    companyEmail.value =
                        companyEmail.value.trim();
                }

                if (companyPhone) {
                    companyPhone.value =
                        companyPhone.value.trim();
                }

                if (companyLocation) {
                    companyLocation.value =
                        companyLocation.value.trim();
                }

                if (companyDescription) {
                    companyDescription.value =
                        companyDescription.value.trim();
                }

                if (name) {
                    name.value =
                        name.value.trim();
                }

                const validCompanyName =
                    validateRequired(
                        companyName,
                        companyNameError,
                        "Company name is required."
                    );

                const validEmail =
                    validateEmail();

                const validPhone =
                    validatePhone();

                const validLocation =
                    validateRequired(
                        companyLocation,
                        companyLocationError,
                        "Company location is required."
                    );

                const validName =
                    validateRequired(
                        name,
                        nameError,
                        "Your name is required."
                    );

                const validPhoto =
                    validatePhoto();

                if (
                    !validCompanyName ||
                    !validEmail ||
                    !validPhone ||
                    !validLocation ||
                    !validName ||
                    !validPhoto
                ) {

                    event.preventDefault();

                    const firstInvalid =
                        form.querySelector(
                            ".client-invalid"
                        );

                    if (firstInvalid) {
                        firstInvalid.focus();
                    }

                    return;
                }

                const saveButton =
                    form.querySelector(".save-btn");

                if (saveButton) {
                    saveButton.disabled = true;
                    saveButton.innerHTML =
                        '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';
                }

            }
        );

    }

});
