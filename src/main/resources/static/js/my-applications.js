document.addEventListener("DOMContentLoaded", function () {

    const maxLength = 180;

    document.querySelectorAll(".cover-letter").forEach(function (container) {

        const textElement = container.querySelector(".cover-letter-text");
        const seeMore = container.querySelector(".see-more");

        const fullText = textElement.dataset.fullText || "";

        if (fullText.length <= maxLength) {

            textElement.textContent = fullText;

            return;
        }


        let isExpanded = false;

        const shortText =
            fullText.substring(0, maxLength).trim() + "...";


        textElement.textContent = shortText;

        seeMore.style.display = "inline-block";


        seeMore.addEventListener("click", function (event) {

            event.preventDefault();

            if (!isExpanded) {

                textElement.textContent = fullText;

                seeMore.textContent = "See less";

                isExpanded = true;

            } else {

                textElement.textContent = shortText;

                seeMore.textContent = "See more";

                isExpanded = false;

            }

        });

    });

});