function toggleDescription(button) {

    const description = button.closest('.description');
    description.classList.toggle('expanded');

    if (description.classList.contains('expanded')) {
        button.textContent = 'See less';
    } else {
        button.textContent = 'See more';
    }

}

let selectedForm = null;

function openConfirmDialog(button, action) {

    selectedForm = button.closest('form');

    const dialog = document.getElementById('confirmDialog');
    const icon = document.getElementById('confirmIcon');
    const title = document.getElementById('confirmTitle');
    const message = document.getElementById('confirmMessage');
    const submitButton = document.getElementById('confirmSubmit');

    icon.classList.remove('reject');
    submitButton.classList.remove('reject');

    if (action === 'accept') {

        icon.innerHTML = '<i class="fa-solid fa-circle-check"></i>';

        title.textContent = 'Accept Job Offer?';

        message.textContent =
            'Are you sure you want to accept this job offer?';

        submitButton.textContent = 'Accept';

    } else {

        icon.innerHTML = '<i class="fa-solid fa-circle-xmark"></i>';

        icon.classList.add('reject');

        title.textContent = 'Reject Job Offer?';

        message.textContent =
            'Are you sure you want to reject this job offer?';

        submitButton.textContent = 'Reject';

        submitButton.classList.add('reject');
    }

    dialog.classList.add('show');

}

function closeConfirmDialog() {

    const dialog = document.getElementById('confirmDialog');

    dialog.classList.remove('show');

    selectedForm = null;

}

document.getElementById('confirmSubmit').addEventListener('click', function () {

    if (selectedForm) {
        selectedForm.submit();
    }

});

document.getElementById('confirmDialog').addEventListener('click', function (event) {

    if (event.target === this) {
        closeConfirmDialog();
    }

});

document.addEventListener('keydown', function (event) {

    if (event.key === 'Escape') {
        closeConfirmDialog();
    }

});
