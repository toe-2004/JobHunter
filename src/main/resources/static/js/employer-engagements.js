document.addEventListener("DOMContentLoaded", function () {

    const descriptions =
        document.querySelectorAll(".description-text");

    descriptions.forEach(function (description) {

        const button =
            description.parentElement.querySelector(".see-more-btn");

        if (!button) {
            return;
        }

        const styles =
            window.getComputedStyle(description);

        const lineHeight =
            parseFloat(styles.lineHeight);

        const maxHeight =
            lineHeight * 2;

        if (description.scrollHeight > maxHeight + 2) {

            button.style.display = "inline-block";

        } else {

            button.style.display = "none";

        }

        button.addEventListener("click", function () {

            const expanded =
                description.classList.toggle("expanded");

            button.textContent =
                expanded ? "See less" : "See more";

        });

    });

});