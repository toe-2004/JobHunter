document.addEventListener("DOMContentLoaded", function () {

document.querySelectorAll(".description-wrapper").forEach(function (wrapper) {

    const description = wrapper.querySelector(".description");
    const button = wrapper.querySelector(".description-toggle");

    description.classList.add("expanded");

    const fullHeight = description.scrollHeight;

    description.classList.remove("expanded");

    const collapsedHeight = description.clientHeight;

    if (fullHeight > collapsedHeight + 2) {
        button.style.display = "inline-block";
    }

});

const confirmationModal =
    document.getElementById("confirmationModal");

const confirmationTitle =
    document.getElementById("confirmationTitle");

const confirmationMessage =
    document.getElementById("confirmationMessage");

const confirmationIcon =
    document.getElementById("confirmationIcon");

const confirmationCancel =
    document.getElementById("confirmationCancel");

const confirmationConfirm =
    document.getElementById("confirmationConfirm");

let currentForm = null;

document.querySelectorAll(".status-action-btn").forEach(function (button) {

    button.addEventListener("click", function () {

        currentForm = this.closest("form");

        const status = this.dataset.status;

        confirmationIcon.classList.remove("delete-icon");
        confirmationConfirm.classList.remove("delete-confirm");

        if (status === "OPEN") {

            confirmationTitle.textContent =
                "Close Job";

            confirmationMessage.textContent =
                "Are you sure you want to close job?";

            confirmationConfirm.textContent =
                "Close Job";

            confirmationIcon.innerHTML =
                '<i class="fas fa-lock"></i>';

        } else {

            confirmationTitle.textContent =
                "Open Job";

            confirmationMessage.textContent =
                "Are you sure you want to open job?";

            confirmationConfirm.textContent =
                "Open Job";

            confirmationIcon.innerHTML =
                '<i class="fas fa-lock-open"></i>';

        }

        confirmationModal.classList.add("active");

    });

});

document.querySelectorAll(".delete-action-btn").forEach(function (button) {

    button.addEventListener("click", function () {

        currentForm = this.closest("form");

        confirmationTitle.textContent =
            "Delete Job";

        confirmationMessage.textContent =
            "Are you sure you want to delete this job? This action cannot be undone.";

        confirmationIcon.classList.add("delete-icon");

        confirmationIcon.innerHTML =
            '<i class="fas fa-trash"></i>';

        confirmationConfirm.classList.add("delete-confirm");

        confirmationConfirm.textContent =
            "Delete";

        confirmationModal.classList.add("active");

    });

});

function closeConfirmation() {

    confirmationModal.classList.remove("active");

    currentForm = null;

}

confirmationCancel.addEventListener(
    "click",
    closeConfirmation
);

confirmationModal.addEventListener(
    "click",
    function (event) {

        if (event.target === confirmationModal) {
            closeConfirmation();
        }

    }
);

document.addEventListener(
    "keydown",
    function (event) {

        if (
            event.key === "Escape" &&
            confirmationModal.classList.contains("active")
        ) {

            closeConfirmation();

        }

    }
);

confirmationConfirm.addEventListener(
    "click",
    function () {

        if (currentForm) {
            currentForm.submit();
        }

    }
);

});

function toggleDescription(button) {

const wrapper =
    button.closest(".description-wrapper");

const description =
    wrapper.querySelector(".description");

if (description.classList.contains("expanded")) {

    description.classList.remove("expanded");

    button.textContent = "See more";

} else {

    description.classList.add("expanded");

    button.textContent = "See less";

}

}
