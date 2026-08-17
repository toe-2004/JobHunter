const openSkillBtn = document.getElementById("openSkillBtn");
const closeSkillBtn = document.getElementById("closeSkillBtn");
const cancelSkillBtn = document.getElementById("cancelSkillBtn");
const skillModal = document.getElementById("skillModal");

openSkillBtn.addEventListener("click",function () {
        skillModal.classList.add("active");
    }
);

closeSkillBtn.addEventListener("click",function () {
        skillModal.classList.remove("active");
    }
);

cancelSkillBtn.addEventListener("click",function () {
        skillModal.classList.remove("active");
    }
);

skillModal.addEventListener("click",function (event) {
        if (event.target === skillModal) {
            skillModal.classList.remove(
                "active"
            );
        }
    }
);

document.addEventListener("keydown",
    function (event) {
        if (event.key === "Escape") {
            skillModal.classList.remove(
                "active"
            );
        }
    }
);

const skillDropdown = document.querySelector(".skill-dropdown");
const skillDropdownBtn = document.getElementById("skillDropdownBtn");
const skillDropdownMenu = document.getElementById("skillDropdownMenu");
const selectedSkillsText = document.getElementById("selectedSkillsText");
const selectedSkillInputs = document.getElementById("selectedSkillInputs");

let selectedSkills = new Set();

document.querySelectorAll(".skill-option.selected").forEach(option => {
    selectedSkills.add(option.dataset.id);
});

skillDropdownBtn.addEventListener("click", function () {
    skillDropdown.classList.toggle("open");
});

document.querySelectorAll(".skill-option").forEach(option => {
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
    const selectedNames = [];
    document.querySelectorAll(".skill-option.selected").forEach(option => {
        selectedNames.push(
            option.querySelector("span:last-child").textContent.trim()
        );
    });

    if (selectedNames.length === 0) {
        selectedSkillsText.textContent = "Select your skills";
    } else {
        selectedSkillsText.textContent = selectedNames.join(", ");
    }

    selectedSkillInputs.innerHTML = "";
    selectedSkills.forEach(skillId => {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "skillIds";
        input.value = skillId;
        selectedSkillInputs.appendChild(input);
    });
}

document.addEventListener("click", function(event) {
    if (!skillDropdown.contains(event.target)) {
        skillDropdown.classList.remove("open");
    }
});

updateSelectedSkills();