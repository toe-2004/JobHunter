const photoInput =
    document.getElementById("profilePhoto");

const photoPreview =
    document.getElementById("photoPreview");

const photoPlaceholder =
    document.getElementById("photoPlaceholder");

photoInput.addEventListener("change", function () {
    const file = this.files[0];

    if (!file) {
        return;
    }

    const reader = new FileReader();

    reader.onload = function (e) {
        if (photoPreview) {
            photoPreview.src = e.target.result;
            photoPreview.style.display = "block";
        } else {
            const img =
                document.createElement("img");

            img.src = e.target.result;
            img.id = "photoPreview";
            img.className = "profile-photo";
            photoPlaceholder.replaceWith(img);
        }
    };

    reader.readAsDataURL(file);
});