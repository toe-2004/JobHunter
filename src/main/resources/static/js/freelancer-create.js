const openSkillBtn =
    document.getElementById("openSkillBtn");

const closeSkillBtn =
    document.getElementById("closeSkillBtn");

const cancelSkillBtn =
    document.getElementById("cancelSkillBtn");

const skillModal =
    document.getElementById("skillModal");

if (openSkillBtn && skillModal) {
    openSkillBtn.addEventListener("click", function () {
        skillModal.classList.add("show");
    });
}

if (closeSkillBtn && skillModal) {
    closeSkillBtn.addEventListener("click", function () {
        skillModal.classList.remove("show");
    });
}

if (cancelSkillBtn && skillModal) {
    cancelSkillBtn.addEventListener("click", function () {
        skillModal.classList.remove("show");
    });
}

if (skillModal) {
    skillModal.addEventListener("click", function (event) {
        if (event.target === skillModal) {
            skillModal.classList.remove("show");
        }
    });
}

document.addEventListener("keydown", function (event) {
    if (event.key === "Escape" && skillModal) {
        skillModal.classList.remove("show");
    }
});

const skillDropdown =
    document.querySelector(".skill-dropdown");

const skillDropdownBtn =
    document.getElementById("skillDropdownBtn");

const skillDropdownMenu =
    document.getElementById("skillDropdownMenu");

const selectedSkillsText =
    document.getElementById("selectedSkillsText");

const selectedSkillInputs =
    document.getElementById("selectedSkillInputs");

let selectedSkills = new Set();

document
    .querySelectorAll(".skill-option.selected")
    .forEach(function (option) {
        selectedSkills.add(
            option.dataset.id
        );
    });

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

                if (selectedSkills.has(skillId)) {
                    selectedSkills.delete(skillId);
                    this.classList.remove(
                        "selected"
                    );
                } else {
                    selectedSkills.add(skillId);
                    this.classList.add(
                        "selected"
                    );
                }

                updateSelectedSkills();
            }
        );
    });

function updateSelectedSkills() {
    if (!selectedSkillsText ||
        !selectedSkillInputs) {
        return;
    }

    const selectedNames = [];

    document
        .querySelectorAll(".skill-option.selected")
        .forEach(function (option) {
            const nameElement =
                option.querySelector(
                    "span:last-child"
                );

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

        selectedSkillInputs.appendChild(
            input
        );
    });
}

document.addEventListener(
    "click",
    function (event) {
        if (
            skillDropdown &&
            !skillDropdown.contains(event.target)
        ) {
            skillDropdown.classList.remove(
                "open"
            );
        }
    }
);

updateSelectedSkills();

const profilePhoto =
    document.getElementById("profilePhoto");

const profilePreview =
    document.getElementById("profilePreview");

const photoPlaceholder =
    document.getElementById("photoPlaceholder");

const fileName =
    document.getElementById("fileName");

if (profilePhoto) {
    profilePhoto.addEventListener(
        "change",
        function () {
            const file =
                this.files[0];

            if (!file) {
                return;
            }

            if (fileName) {
                fileName.textContent =
                    file.name;
            }

            const reader =
                new FileReader();

            reader.onload =
                function (event) {
                    if (profilePreview) {
                        profilePreview.src =
                            event.target.result;
                    }
                    else if (photoPlaceholder) {
                        photoPlaceholder.innerHTML = `
                            <img
                                src="${event.target.result}"
                                class="profile-preview"
                                alt="Profile Preview">
                        `;
                    }
                };

            reader.readAsDataURL(file);
        }
    );
}