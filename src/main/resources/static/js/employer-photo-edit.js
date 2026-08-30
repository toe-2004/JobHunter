document.addEventListener("DOMContentLoaded", function () {

    const photoInput =
        document.getElementById("profilePhotoFile");

    const photoContainer =
        document.getElementById("photoContainer");

    const selectedFileName =
        document.getElementById("selectedFileName");

    const photoError =
        document.getElementById("photoError");


    if (!photoInput) {
        return;
    }


    photoInput.addEventListener("change", function () {

        const file = this.files[0];

        // Clear previous messages
        if (photoError) {
            photoError.textContent = "";
        }

        if (selectedFileName) {
            selectedFileName.textContent = "";
        }


        if (!file) {
            return;
        }


        const allowedTypes = [
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
        ];


        if (!allowedTypes.includes(file.type)) {

            if (photoError) {
                photoError.textContent =
                    "Please select JPG, JPEG, PNG or WEBP.";
            }

            this.value = "";
            return;
        }

        const maxSize = 5 * 1024 * 1024;

        if (file.size > maxSize) {

            if (photoError) {
                photoError.textContent =
                    "Image size must be less than 5 MB.";
            }

            this.value = "";
            return;
        }


        if (selectedFileName) {
            selectedFileName.textContent = file.name;
        }


        const reader = new FileReader();

        reader.onload = function (event) {

            if (!photoContainer) {
                return;
            }


            let image =
                document.getElementById("photoPreview");


            if (!image) {

                image =
                    document.createElement("img");

                image.id = "photoPreview";

                image.className =
                    "com-profile-photo";

                image.alt = "Company Photo";


                photoContainer.innerHTML = "";

                photoContainer.appendChild(image);
            }

            image.src = event.target.result;
        };


        reader.readAsDataURL(file);
    });

});