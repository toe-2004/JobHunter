document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("freelancerProfileForm");

    const profilePhoto = document.getElementById("profilePhoto");
    const profilePreview = document.getElementById("profilePreview");
    const photoPlaceholder = document.getElementById("photoPlaceholder");
    const fileName = document.getElementById("fileName");
    const photoError = document.getElementById("photoError");

    const title = document.getElementById("title");
    const phone = document.getElementById("phone");
    const location = document.getElementById("location");
    const summary = document.getElementById("summary");
    const education = document.getElementById("education");
    const experience = document.getElementById("experience");

    const skillDropdown = document.getElementById("skillDropdown");
    const skillDropdownBtn = document.getElementById("skillDropdownBtn");
    const selectedSkillsText = document.getElementById("selectedSkillsText");
    const selectedSkillInputs = document.getElementById("selectedSkillInputs");
    const skillError = document.getElementById("skillError");

    const skillModal = document.getElementById("skillModal");
    const openSkillBtn = document.getElementById("openSkillBtn");
    const closeSkillBtn = document.getElementById("closeSkillBtn");
    const cancelSkillBtn = document.getElementById("cancelSkillBtn");
    const cancelProfileBtn = document.getElementById("cancelProfileBtn");

    const skillForm = document.getElementById("skillForm");
    const skillName = document.getElementById("skillName");
    const skillNameError = document.getElementById("skillNameError");

    const STORAGE_KEY = "freelancerProfileDraft";
    const NEW_SKILL_KEY = "freelancerNewSkill";

    let selectedSkills = new Set();

    document
        .querySelectorAll(".skill-option.selected")
        .forEach(function (option) {
            if (option.dataset.id) {
                selectedSkills.add(option.dataset.id);
            }
        });

    function setError(input, errorElement, message) {
        if (input) {
            input.classList.add("invalid");
        }

        if (errorElement) {
            errorElement.textContent = message;
        }
    }

    function clearError(input, errorElement) {
        if (input) {
            input.classList.remove("invalid");
        }

        if (errorElement) {
            errorElement.textContent = "";
        }
    }

    function validateRequired(input, errorElement, message) {
        if (!input) {
            return false;
        }

        if (input.value.trim() === "") {
            setError(input, errorElement, message);
            return false;
        }

        clearError(input, errorElement);
        return true;
    }

    function validatePhone() {
        if (!phone) {
            return true;
        }

        const value = phone.value.trim();
        const phonePattern = /^(09|\+959)[\s-]?\d{7,9}$/;

        if (value === "") {
            setError(
                phone,
                document.getElementById("phoneError"),
                "Phone number is required."
            );
            return false;
        }

        if (!phonePattern.test(value)) {
            setError(
                phone,
                document.getElementById("phoneError"),
                "Please enter a valid Myanmar phone number."
            );
            return false;
        }

        clearError(
            phone,
            document.getElementById("phoneError")
        );

        return true;
    }

    function validatePhoto() {
        if (!profilePhoto || !profilePhoto.files.length) {
            clearError(null, photoError);
            return true;
        }

        const file = profilePhoto.files[0];

        const allowedTypes = [
            "image/jpeg",
            "image/png",
            "image/webp"
        ];

        const maxSize = 5 * 1024 * 1024;

        if (!allowedTypes.includes(file.type)) {
            setError(
                null,
                photoError,
                "Only JPG, JPEG, PNG or WEBP images are allowed."
            );
            return false;
        }

        if (file.size > maxSize) {
            setError(
                null,
                photoError,
                "Image size must be less than 5MB."
            );
            return false;
        }

        clearError(null, photoError);

        return true;
    }

    function validateSkills() {
        if (selectedSkills.size === 0) {
            if (skillError) {
                skillError.textContent = "Please select at least one skill.";
            }

            return false;
        }

        if (skillError) {
            skillError.textContent = "";
        }

        return true;
    }

    function updateSelectedSkills() {
        if (!selectedSkillsText || !selectedSkillInputs) {
            return;
        }

        const selectedNames = [];

        document
            .querySelectorAll(".skill-option.selected")
            .forEach(function (option) {

                const nameElement =
                    option.querySelector("span:last-child");

                if (nameElement) {
                    selectedNames.push(
                        nameElement.textContent.trim()
                    );
                }
            });

        if (selectedNames.length === 0) {
            selectedSkillsText.textContent =
                "Select your skills";
        } else {
            selectedSkillsText.textContent =
                selectedNames.join(", ");
        }

        selectedSkillInputs.innerHTML = "";

        selectedSkills.forEach(function (skillId) {

            const input =
                document.createElement("input");

            input.type = "hidden";
            input.name = "skillIds";
            input.value = skillId;

            selectedSkillInputs.appendChild(input);
        });
    }

    function saveDraft() {

        const draft = {
            title: title ? title.value : "",
            phone: phone ? phone.value : "",
            location: location ? location.value : "",
            summary: summary ? summary.value : "",
            education: education ? education.value : "",
            experience: experience ? experience.value : "",
            selectedSkills: Array.from(selectedSkills)
        };

        if (
            profilePreview &&
            profilePreview.src &&
            profilePreview.src.startsWith("data:image")
        ) {
            draft.photoPreview = profilePreview.src;
        }

        localStorage.setItem(
            STORAGE_KEY,
            JSON.stringify(draft)
        );
    }

    function restoreDraft() {

        const savedDraft =
            localStorage.getItem(STORAGE_KEY);

        if (!savedDraft) {
            return;
        }

        let draft;

        try {
            draft = JSON.parse(savedDraft);
        } catch (error) {
            localStorage.removeItem(STORAGE_KEY);
            return;
        }

        if (title && draft.title !== undefined) {
            title.value = draft.title;
        }

        if (phone && draft.phone !== undefined) {
            phone.value = draft.phone;
        }

        if (location && draft.location !== undefined) {
            location.value = draft.location;
        }

        if (summary && draft.summary !== undefined) {
            summary.value = draft.summary;
        }

        if (education && draft.education !== undefined) {
            education.value = draft.education;
        }

        if (experience && draft.experience !== undefined) {
            experience.value = draft.experience;
        }

        if (Array.isArray(draft.selectedSkills)) {

            draft.selectedSkills.forEach(function (skillId) {

                selectedSkills.add(String(skillId));

                const option =
                    document.querySelector(
                        '.skill-option[data-id="' +
                        skillId +
                        '"]'
                    );

                if (option) {
                    option.classList.add("selected");
                }
            });
        }

        if (draft.photoPreview) {

            if (profilePreview) {

                profilePreview.src =
                    draft.photoPreview;

                profilePreview.style.display =
                    "block";

                if (photoPlaceholder) {
                    photoPlaceholder.style.display =
                        "none";
                }

            } else if (photoPlaceholder) {

                const image =
                    document.createElement("img");

                image.src =
                    draft.photoPreview;

                image.id =
                    "profilePreview";

                image.alt =
                    "Profile Preview";

                photoPlaceholder.replaceWith(image);
            }
        }

        updateSelectedSkills();
    }

    function selectNewSkill() {

        const newSkillName =
            localStorage.getItem(NEW_SKILL_KEY);

        if (!newSkillName) {
            return;
        }

        const normalizedName =
            newSkillName.trim().toLowerCase();

        let found = false;

        document
            .querySelectorAll(".skill-option")
            .forEach(function (option) {

                const nameElement =
                    option.querySelector("span:last-child");

                if (!nameElement) {
                    return;
                }

                const currentName =
                    nameElement.textContent
                        .trim()
                        .toLowerCase();

                if (currentName === normalizedName) {

                    const skillId =
                        option.dataset.id;

                    if (skillId) {

                        selectedSkills.add(skillId);
                        option.classList.add("selected");

                        found = true;
                    }
                }
            });

        if (found) {
            updateSelectedSkills();
            saveDraft();
        }

        localStorage.removeItem(NEW_SKILL_KEY);
    }

    if (skillDropdownBtn && skillDropdown) {

        skillDropdownBtn.addEventListener(
            "click",
            function (event) {

                event.stopPropagation();

                skillDropdown.classList.toggle("open");
            }
        );
    }

    document
        .querySelectorAll(".skill-option")
        .forEach(function (option) {

            option.addEventListener(
                "click",
                function () {

                    const skillId =
                        this.dataset.id;

                    if (!skillId) {
                        return;
                    }

                    if (selectedSkills.has(skillId)) {

                        selectedSkills.delete(skillId);
                        this.classList.remove("selected");

                    } else {

                        selectedSkills.add(skillId);
                        this.classList.add("selected");
                    }

                    if (skillError) {
                        skillError.textContent = "";
                    }

                    updateSelectedSkills();
                    saveDraft();
                }
            );
        });

    document.addEventListener(
        "click",
        function (event) {

            if (
                skillDropdown &&
                !skillDropdown.contains(event.target)
            ) {
                skillDropdown.classList.remove("open");
            }
        }
    );

    if (profilePhoto) {

        profilePhoto.addEventListener(
            "change",
            function () {

                const file =
                    this.files[0];

                if (!file) {

                    if (fileName) {
                        fileName.textContent =
                            "No file selected";
                    }

                    return;
                }

                if (fileName) {
                    fileName.textContent =
                        file.name;
                }

                if (!validatePhoto()) {
                    this.value = "";
                    return;
                }

                const reader =
                    new FileReader();

                reader.onload =
                    function (event) {

                        if (profilePreview) {

                            profilePreview.src =
                                event.target.result;

                            profilePreview.style.display =
                                "block";

                            if (photoPlaceholder) {
                                photoPlaceholder.style.display =
                                    "none";
                            }

                        } else if (photoPlaceholder) {

                            const image =
                                document.createElement("img");

                            image.src =
                                event.target.result;

                            image.id =
                                "profilePreview";

                            image.alt =
                                "Profile Preview";

                            photoPlaceholder.replaceWith(image);
                        }

                        saveDraft();
                    };

                reader.readAsDataURL(file);
            }
        );
    }

    if (phone) {

        phone.addEventListener(
            "input",
            function () {

                this.value =
                    this.value.replace(
                        /[^0-9+\-\s]/g,
                        ""
                    );

                clearError(
                    phone,
                    document.getElementById("phoneError")
                );

                saveDraft();
            }
        );

        phone.addEventListener(
            "blur",
            validatePhone
        );
    }

    if (title) {

        title.addEventListener(
            "input",
            function () {

                clearError(
                    title,
                    document.getElementById("titleError")
                );

                saveDraft();
            }
        );
    }

    if (location) {

        location.addEventListener(
            "input",
            function () {

                clearError(
                    location,
                    document.getElementById("locationError")
                );

                saveDraft();
            }
        );
    }

    if (summary) {

        summary.addEventListener(
            "input",
            function () {

                clearError(
                    summary,
                    document.getElementById("summaryError")
                );

                saveDraft();
            }
        );
    }

    if (education) {

        education.addEventListener(
            "input",
            function () {

                clearError(
                    education,
                    document.getElementById("educationError")
                );

                saveDraft();
            }
        );
    }

    if (experience) {

        experience.addEventListener(
            "input",
            function () {

                clearError(
                    experience,
                    document.getElementById("experienceError")
                );

                saveDraft();
            }
        );
    }

    if (openSkillBtn && skillModal) {

        openSkillBtn.addEventListener(
            "click",
            function () {

                saveDraft();

                skillModal.classList.add("show");

                if (skillName) {

                    setTimeout(
                        function () {
                            skillName.focus();
                        },
                        100
                    );
                }
            }
        );
    }

    function closeSkillModal() {

        if (skillModal) {
            skillModal.classList.remove("show");
        }
    }

    if (closeSkillBtn) {

        closeSkillBtn.addEventListener(
            "click",
            closeSkillModal
        );
    }

    if (cancelSkillBtn) {

        cancelSkillBtn.addEventListener(
            "click",
            closeSkillModal
        );
    }

    if (skillModal) {

        skillModal.addEventListener(
            "click",
            function (event) {

                if (event.target === skillModal) {
                    closeSkillModal();
                }
            }
        );
    }

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Escape" &&
                skillModal &&
                skillModal.classList.contains("show")
            ) {
                closeSkillModal();
            }
        }
    );

    if (skillName) {

        skillName.addEventListener(
            "input",
            function () {

                clearError(
                    skillName,
                    skillNameError
                );
            }
        );
    }

    if (skillForm) {

        skillForm.addEventListener(
            "submit",
            function (event) {

                const value =
                    skillName
                        ? skillName.value.trim()
                        : "";

                if (value === "") {

                    event.preventDefault();

                    setError(
                        skillName,
                        skillNameError,
                        "Skill name is required."
                    );

                    return;
                }

                if (value.length < 2) {

                    event.preventDefault();

                    setError(
                        skillName,
                        skillNameError,
                        "Skill name must be at least 2 characters."
                    );

                    return;
                }

                clearError(
                    skillName,
                    skillNameError
                );

                saveDraft();

                localStorage.setItem(
                    NEW_SKILL_KEY,
                    value
                );
            }
        );
    }

    if (cancelProfileBtn) {

        cancelProfileBtn.addEventListener(
            "click",
            function () {

                localStorage.removeItem(
                    STORAGE_KEY
                );

                localStorage.removeItem(
                    NEW_SKILL_KEY
                );
            }
        );
    }

    if (form) {

        form.addEventListener(
            "submit",
            function (event) {

                let valid = true;

                const titleValid =
                    validateRequired(
                        title,
                        document.getElementById("titleError"),
                        "Professional title is required."
                    );

                const phoneValid =
                    validatePhone();

                const locationValid =
                    validateRequired(
                        location,
                        document.getElementById("locationError"),
                        "Location is required."
                    );

                const summaryValid =
                    validateRequired(
                        summary,
                        document.getElementById("summaryError"),
                        "Professional summary is required."
                    );

                const educationValid =
                    validateRequired(
                        education,
                        document.getElementById("educationError"),
                        "Education background is required."
                    );

                const experienceValid =
                    validateRequired(
                        experience,
                        document.getElementById("experienceError"),
                        "Work experience is required."
                    );

                const photoValid =
                    validatePhoto();

                const skillsValid =
                    validateSkills();

                if (
                    !titleValid ||
                    !phoneValid ||
                    !locationValid ||
                    !summaryValid ||
                    !educationValid ||
                    !experienceValid ||
                    !photoValid ||
                    !skillsValid
                ) {
                    valid = false;
                }

                if (!valid) {

                    event.preventDefault();

                    saveDraft();

                    const firstInvalid =
                        form.querySelector(".invalid");

                    if (firstInvalid) {

                        firstInvalid.scrollIntoView({
                            behavior: "smooth",
                            block: "center"
                        });

                        firstInvalid.focus();
                    }

                } else {

                    localStorage.removeItem(
                        STORAGE_KEY
                    );

                    localStorage.removeItem(
                        NEW_SKILL_KEY
                    );
                }
            }
        );
    }

    restoreDraft();
    selectNewSkill();
    updateSelectedSkills();

});
