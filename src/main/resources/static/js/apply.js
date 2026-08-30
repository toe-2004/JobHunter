document.addEventListener("DOMContentLoaded", function () {

    const cvInput = document.getElementById("curriculumVitae");
    const selectedCvName = document.getElementById("selectedCvName");

    if (cvInput && selectedCvName) {

        cvInput.addEventListener("change", function () {

            if (this.files && this.files.length > 0) {

                selectedCvName.textContent = this.files[0].name;
                selectedCvName.classList.add("show");

            } else {

                selectedCvName.textContent = "";
                selectedCvName.classList.remove("show");

            }

        });

    }

});