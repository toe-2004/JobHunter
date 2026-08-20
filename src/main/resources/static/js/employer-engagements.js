document.addEventListener("DOMContentLoaded", function () {

    const descriptions =
        document.querySelectorAll(".description-text");

    descriptions.forEach(function (description) {

        const button =
            description.parentElement.querySelector(".see-more-btn");

        const styles =
            window.getComputedStyle(description);

        const lineHeight =
            parseFloat(styles.lineHeight);

        const maxHeight =
            lineHeight * 2;

        if (description.scrollHeight > maxHeight + 2) {

            button.style.display = "inline-block";

        }

        button.addEventListener("click", function () {

            if (description.classList.contains("expanded")) {

                description.classList.remove("expanded");

                button.textContent = "See more";

            } else {

                description.classList.add("expanded");

                button.textContent = "See less";

            }

        });

    });

});