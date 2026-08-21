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
});

function toggleDescription(button) {
    const wrapper = button.closest(".description-wrapper");
    const description = wrapper.querySelector(".description");

    if (description.classList.contains("expanded")) {
        description.classList.remove("expanded");
        button.textContent = "See more";
    } else {
        description.classList.add("expanded");
        button.textContent = "See less";
    }
}