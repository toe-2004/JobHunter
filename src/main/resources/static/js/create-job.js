document.addEventListener("DOMContentLoaded", function () {

const openSkillBtn =
    document.getElementById("openSkillBtn");

const closeSkillBtn =
    document.getElementById("closeSkillBtn");

const cancelSkillBtn =
    document.getElementById("cancelSkillBtn");

const skillModal =
    document.getElementById("skillModal");

const skillNameInput =
    document.getElementById("skillName");

function openModal() {

    skillModal.classList.add("active");

    setTimeout(function () {
        skillNameInput.focus();
    }, 150);
}

function closeModal() {

    skillModal.classList.remove("active");

    skillNameInput.value = "";
}

openSkillBtn.addEventListener(
    "click",
    openModal
);

closeSkillBtn.addEventListener(
    "click",
    closeModal
);

cancelSkillBtn.addEventListener(
    "click",
    closeModal
);

skillModal.addEventListener(
    "click",
    function (event) {

        if (event.target === skillModal) {
            closeModal();
        }

    }
);

document.addEventListener(
    "keydown",
    function (event) {

        if (event.key === "Escape") {
            closeModal();
        }

    }
);

const skillDropdown =
    document.getElementById("skillDropdown");

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
            String(option.dataset.id)
        );

    });

skillDropdownBtn.addEventListener(
    "click",
    function () {

        skillDropdown.classList.toggle("open");

    }
);

document.addEventListener(
    "click",
    function (event) {

        if (!skillDropdown.contains(event.target)) {

            skillDropdown.classList.remove(
                "open"
            );

        }

    }
);

function attachSkillOption(option) {

    option.addEventListener(
        "click",
        function () {

            const skillId =
                String(this.dataset.id);

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

}

document
    .querySelectorAll(".skill-option")
    .forEach(attachSkillOption);

function updateSelectedSkills() {

    const selectedNames = [];

    document
        .querySelectorAll(
            ".skill-option.selected"
        )
        .forEach(function (option) {

            const name =
                option
                    .querySelector(
                        "span:last-child"
                    )
                    .textContent
                    .trim();

            selectedNames.push(name);

        });

    if (selectedNames.length === 0) {

        selectedSkillsText.textContent =
            "Select required skills";

    } else {

        selectedSkillsText.textContent =
            selectedNames.join(", ");

    }

    selectedSkillInputs.innerHTML = "";

    selectedSkills.forEach(
        function (skillId) {

            const input =
                document.createElement(
                    "input"
                );

            input.type = "hidden";

            input.name = "skillIds";

            input.value = skillId;

            selectedSkillInputs.appendChild(
                input
            );

        }
    );

}

updateSelectedSkills();

document
    .getElementById("skillForm")
    .addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            const skillName =
                skillNameInput.value.trim();

            if (!skillName) {

                skillNameInput.focus();

                return;

            }

            const csrfMeta =
                document.querySelector(
                    'meta[name="_csrf"]'
                );

            const csrfHeaderMeta =
                document.querySelector(
                    'meta[name="_csrf_header"]'
                );

            if (!csrfMeta || !csrfHeaderMeta) {

                alert(
                    "Security token is missing."
                );

                return;

            }

            const csrfToken =
                csrfMeta.getAttribute(
                    "content"
                );

            const csrfHeader =
                csrfHeaderMeta.getAttribute(
                    "content"
                );

            try {

                const response =
                    await fetch(
                        "/job/skill",
                        {
                            method: "POST",

                            headers: {

                                "Content-Type":
                                    "application/x-www-form-urlencoded",

                                [csrfHeader]:
                                    csrfToken

                            },

                            body:
                                "name=" +
                                encodeURIComponent(
                                    skillName
                                )

                        }
                    );

                if (!response.ok) {

                    const errorText =
                        await response.text();

                    console.error(
                        "Create skill failed:",
                        response.status,
                        errorText
                    );

                    alert(
                        "Unable to create this skill."
                    );

                    return;

                }

                const skill =
                    await response.json();

                const skillId =
                    String(skill.id);

                let existingOption = null;

                document
                    .querySelectorAll(
                        ".skill-option"
                    )
                    .forEach(
                        function (option) {

                            if (
                                String(
                                    option.dataset.id
                                ) === skillId
                            ) {

                                existingOption =
                                    option;

                            }

                        }
                    );

                if (!existingOption) {

                    existingOption =
                        document.createElement(
                            "div"
                        );

                    existingOption.className =
                        "skill-option";

                    existingOption.dataset.id =
                        skillId;

                    existingOption.innerHTML = `

                        <span class="skill-check">

                            <i class="fa-solid fa-check"></i>

                        </span>

                        <span>
                            ${escapeHtml(skill.name)}
                        </span>

                    `;

                    skillDropdownMenu.appendChild(
                        existingOption
                    );

                    attachSkillOption(
                        existingOption
                    );

                }

                selectedSkills.add(
                    skillId
                );

                existingOption.classList.add(
                    "selected"
                );

                updateSelectedSkills();

                closeModal();

                skillDropdown.classList.add(
                    "open"
                );

            } catch (error) {

                console.error(
                    "Create skill error:",
                    error
                );

                alert(
                    "Something went wrong while creating the skill."
                );

            }

        }
    );

function escapeHtml(value) {

    const div =
        document.createElement("div");

    div.textContent = value;

    return div.innerHTML;

}

const employmentType =
    document.getElementById("employmentType");

const hourlyFields =
    document.querySelectorAll(".hourly-field");

const fixedPriceField =
    document.querySelector(".fixed-price-field");

const salaryMin =
    document.getElementById("salaryMin");

const salaryMax =
    document.getElementById("salaryMax");

const budget =
    document.getElementById("budget");

function updateBudgetFields() {

    const selectedType =
        employmentType.value;

    if (selectedType === "HOURLY") {

        hourlyFields.forEach(function (field) {

            field.style.display = "block";

        });

        fixedPriceField.style.display = "none";

        salaryMin.disabled = false;
        salaryMax.disabled = false;

        budget.disabled = true;
        budget.value = "";

    } else if (selectedType === "FIXED_PRICE") {

        hourlyFields.forEach(function (field) {

            field.style.display = "none";

        });

        fixedPriceField.style.display = "block";

        salaryMin.disabled = true;
        salaryMax.disabled = true;

        salaryMin.value = "";
        salaryMax.value = "";

        budget.disabled = false;

    } else {

        hourlyFields.forEach(function (field) {

            field.style.display = "none";

        });

        fixedPriceField.style.display = "none";

        salaryMin.disabled = true;
        salaryMax.disabled = true;
        budget.disabled = true;

    }

}

updateBudgetFields();

employmentType.addEventListener(
    "change",
    updateBudgetFields
);

});
