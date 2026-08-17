document.addEventListener("DOMContentLoaded", function () {

    const openSkillBtn = document.getElementById("openSkillBtn");
    const closeSkillBtn = document.getElementById("closeSkillBtn");
    const cancelSkillBtn = document.getElementById("cancelSkillBtn");
    const skillModal = document.getElementById("skillModal");
    const skillNameInput = document.getElementById("skillName");

    openSkillBtn.addEventListener("click", function () {

        skillModal.classList.add("active");
        skillNameInput.focus();

    });

    closeSkillBtn.addEventListener("click", function () {

        skillModal.classList.remove("active");

    });

    cancelSkillBtn.addEventListener("click", function () {

        skillModal.classList.remove("active");

    });

    skillModal.addEventListener("click", function (event) {

        if (event.target === skillModal) {
            skillModal.classList.remove("active");

        }

    });

    document.addEventListener("keydown", function (event) {

        if (event.key === "Escape") {
            skillModal.classList.remove("active");

        }

    });

    const skillDropdown = document.querySelector(".skill-dropdown");
    const skillDropdownBtn = document.getElementById("skillDropdownBtn");
    const selectedSkillsText = document.getElementById("selectedSkillsText");
    const selectedSkillInputs = document.getElementById("selectedSkillInputs");
    const skillDropdownMenu = document.getElementById("skillDropdownMenu");

    let selectedSkills = new Set();

    document
        .querySelectorAll(".skill-option.selected")
        .forEach(function (option) {

            selectedSkills.add(
                option.dataset.id
            );

        });

    skillDropdownBtn.addEventListener(
        "click",
        function () {

            skillDropdown.classList.toggle("open");

        }
    );

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

        const selectedNames = [];
        document
            .querySelectorAll(".skill-option.selected")
            .forEach(function (option) {

                const name =
                    option
                        .querySelector("span:last-child")
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

        selectedSkills.forEach(function (skillId) {

            const input =
                document.createElement("input");

            input.type = "hidden";

            input.name = "skillIds";

            input.value = skillId;

            selectedSkillInputs.appendChild(input);

        });

    }

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

    updateSelectedSkills();

    document
        .getElementById("skillForm")
        .addEventListener(
            "submit",
            async function (event) {

                event.preventDefault();

                const skillName =
                    skillNameInput.value.trim();

                if (skillName === "") {

                    return;

                }

                try {

                    const csrfToken =
                        document
                            .querySelector(
                                'meta[name="_csrf"]'
                            )
                            .getAttribute("content");


                    const csrfHeader =
                        document
                            .querySelector(
                                'meta[name="_csrf_header"]'
                            )
                            .getAttribute("content");

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
                            "Status:",
                            response.status
                        );

                        console.error(
                            "Response:",
                            errorText
                        );

                        alert(
                            "Unable to create skill.\n\n" +
                            "Status: " +
                            response.status
                        );

                        return;

                    }

                    const skill =
                        await response.json();

                    let existingOption = null;


                    document
                        .querySelectorAll(
                            ".skill-option"
                        )
                        .forEach(function (option) {

                            if (
                                option.dataset.id ===
                                String(skill.id)
                            ) {

                                existingOption =
                                    option;

                            }

                        });

                    if (!existingOption) {

                        const option =
                            document.createElement(
                                "div"
                            );


                        option.className =
                            "skill-option";


                        option.dataset.id =
                            String(skill.id);


                        option.innerHTML = `
                            <span class="skill-check">
                                ✓
                            </span>

                            <span>
                                ${skill.name}
                            </span>
                        `;


                        skillDropdownMenu.appendChild(
                            option
                        );

                        option.addEventListener(
                            "click",
                            function () {

                                const skillId =
                                    this.dataset.id;


                                if (
                                    selectedSkills.has(
                                        skillId
                                    )
                                ) {

                                    selectedSkills.delete(
                                        skillId
                                    );

                                    this.classList.remove(
                                        "selected"
                                    );

                                } else {

                                    selectedSkills.add(
                                        skillId
                                    );

                                    this.classList.add(
                                        "selected"
                                    );

                                }

                                updateSelectedSkills();

                            }
                        );

                        existingOption =
                            option;

                    }

                    selectedSkills.add(
                        String(skill.id)
                    );

                    existingOption.classList.add(
                        "selected"
                    );

                    updateSelectedSkills();

                    skillModal.classList.remove(
                        "active"
                    );

                    skillNameInput.value = "";

                } catch (error) {

                    console.error(error);

                    alert(
                        "Something went wrong while creating the skill."
                    );

                }

            }
        );

});