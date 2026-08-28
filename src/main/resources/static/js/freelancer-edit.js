document.addEventListener("DOMContentLoaded", function () {

    const openSkillBtn = document.getElementById("openSkillBtn");
    const closeSkillBtn = document.getElementById("closeSkillBtn");
    const cancelSkillBtn = document.getElementById("cancelSkillBtn");
    const skillModal = document.getElementById("skillModal");

    const skillDropdown = document.getElementById("skillDropdown");
    const skillDropdownBtn = document.getElementById("skillDropdownBtn");
    const selectedSkillsText = document.getElementById("selectedSkillsText");
    const selectedSkillInputs = document.getElementById("selectedSkillInputs");

    const profilePhoto = document.getElementById("profilePhoto");
    const profilePreview = document.getElementById("profilePreview");
    const photoPlaceholder = document.getElementById("photoPlaceholder");
    const fileName = document.getElementById("fileName");

    const phoneInput = document.getElementById("phone");
    const phoneError = document.getElementById("phoneError");

    const selectedSkills = new Set();

    document.querySelectorAll(".skill-option.selected").forEach(function (option) {
        selectedSkills.add(option.dataset.id);
    });

    function closeSkillModal() {
        if (skillModal) {
            skillModal.classList.remove("show");
            document.body.style.overflow = "";
        }
    }

    function openSkillModal() {
        if (skillModal) {
            skillModal.classList.add("show");
            document.body.style.overflow = "hidden";

            const skillName = document.getElementById("skillName");

            if (skillName) {
                setTimeout(function () {
                    skillName.focus();
                }, 100);
            }
        }
    }

    if (openSkillBtn) {
        openSkillBtn.addEventListener("click", openSkillModal);
    }

    if (closeSkillBtn) {
        closeSkillBtn.addEventListener("click", closeSkillModal);
    }

    if (cancelSkillBtn) {
        cancelSkillBtn.addEventListener("click", closeSkillModal);
    }

    if (skillModal) {
        skillModal.addEventListener("click", function (event) {
            if (event.target === skillModal) {
                closeSkillModal();
            }
        });
    }

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            closeSkillModal();

            if (skillDropdown) {
                skillDropdown.classList.remove("open");
            }
        }
    });

    if (skillDropdownBtn && skillDropdown) {
        skillDropdownBtn.addEventListener("click", function (event) {
            event.stopPropagation();
            skillDropdown.classList.toggle("open");
        });
    }

    document.querySelectorAll(".skill-option").forEach(function (option) {
        option.addEventListener("click", function () {

            const skillId = this.dataset.id;

            if (selectedSkills.has(skillId)) {
                selectedSkills.delete(skillId);
                this.classList.remove("selected");
            } else {
                selectedSkills.add(skillId);
                this.classList.add("selected");
            }

            updateSelectedSkills();
        });
    });

    function updateSelectedSkills() {

        if (!selectedSkillsText || !selectedSkillInputs) {
            return;
        }

        const selectedNames = [];

        document.querySelectorAll(".skill-option.selected").forEach(function (option) {

            const nameElement = option.querySelector("span:last-child");

            if (nameElement) {
                selectedNames.push(nameElement.textContent.trim());
            }
        });

        if (selectedNames.length === 0) {
            selectedSkillsText.textContent = "Select your skills";
        } else {
            selectedSkillsText.textContent = selectedNames.join(", ");
        }

        selectedSkillInputs.innerHTML = "";

        selectedSkills.forEach(function (skillId) {

            const input = document.createElement("input");

            input.type = "hidden";
            input.name = "skillIds";
            input.value = skillId;

            selectedSkillInputs.appendChild(input);
        });
    }

    document.addEventListener("click", function (event) {

        if (
            skillDropdown &&
            !skillDropdown.contains(event.target)
        ) {
            skillDropdown.classList.remove("open");
        }

    });

    updateSelectedSkills();

    if (phoneInput) {

        phoneInput.addEventListener("input", function () {

            this.value = this.value.replace(/\D/g, "");

            if (this.value.length > 15) {
                this.value = this.value.substring(0, 15);
            }

            validatePhone();
        });

        phoneInput.addEventListener("blur", function () {
            validatePhone();
        });
    }

    function validatePhone() {

        if (!phoneInput) {
            return true;
        }

        const value = phoneInput.value.trim();

        phoneInput.classList.remove("input-error");

        if (phoneError) {
            phoneError.textContent = "";
        }

        if (value === "") {
            return true;
        }

        if (!/^[0-9]+$/.test(value)) {

            phoneInput.classList.add("input-error");

            if (phoneError) {
                phoneError.textContent =
                    "Phone number must contain numbers only.";
            }

            return false;
        }

        if (value.length < 7 || value.length > 15) {

            phoneInput.classList.add("input-error");

            if (phoneError) {
                phoneError.textContent =
                    "Phone number must be between 7 and 15 digits.";
            }

            return false;
        }

        return true;
    }

    if (profilePhoto) {

        profilePhoto.addEventListener("change", function () {

            const file = this.files[0];

            if (!file) {
                return;
            }

            const allowedTypes = [
                "image/jpeg",
                "image/jpg",
                "image/png"
            ];

            if (!allowedTypes.includes(file.type)) {

                this.value = "";

                if (fileName) {
                    fileName.textContent =
                        "Please select a JPG, JPEG or PNG image.";
                }

                return;
            }

            const maxSize = 5 * 1024 * 1024;

            if (file.size > maxSize) {

                this.value = "";

                if (fileName) {
                    fileName.textContent =
                        "Image size must be less than 5 MB.";
                }

                return;
            }

            if (fileName) {
                fileName.textContent = file.name;
            }

            const reader = new FileReader();

            reader.onload = function (event) {

                if (profilePreview) {

                    profilePreview.src =
                        event.target.result;

                } else if (photoPlaceholder) {

                    photoPlaceholder.innerHTML = "";

                    const image =
                        document.createElement("img");

                    image.src =
                        event.target.result;

                    image.alt =
                        "Profile Preview";

                    photoPlaceholder.appendChild(image);
                }
            };

            reader.readAsDataURL(file);
        });
    }

    const profileForm =
        document.querySelector(
            'form[enctype="multipart/form-data"]'
        );

    if (profileForm) {

        profileForm.addEventListener("submit", function (event) {

            if (!validatePhone()) {
                event.preventDefault();

                phoneInput.focus();

                return;
            }

            const titleInput =
                document.getElementById("title");

            if (titleInput) {
                titleInput.value =
                    titleInput.value.trim();
            }

            const locationInput =
                document.getElementById("location");

            if (locationInput) {
                locationInput.value =
                    locationInput.value.trim();
            }

            const summary =
                document.getElementById("summary");

            if (summary) {
                summary.value =
                    summary.value.trim();
            }

            const education =
                document.getElementById("education");

            if (education) {
                education.value =
                    education.value.trim();
            }

            const experience =
                document.getElementById("experience");

            if (experience) {
                experience.value =
                    experience.value.trim();
            }

        });
    }


});
