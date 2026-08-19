function toggleDescription(button) {

    const description = button.closest('.description');
    description.classList.toggle('expanded');

    if (description.classList.contains('expanded')) {
        button.textContent = 'See less';
    } else {
        button.textContent = 'See more';
    }

}