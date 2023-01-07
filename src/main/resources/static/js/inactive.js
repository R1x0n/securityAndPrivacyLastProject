function inactivityTime () {
    var time;
    window.onload = resetTimer;
    // DOM Events
    document.onmousemove = resetTimer;
    document.onkeydown = resetTimer;

    function logout() {
        location.href = 'app/logout'
    }

    function resetTimer() {
        clearTimeout(time);
        time = setTimeout(logout, 120000)
        // 120000 milliseconds = 1 second
    }
};

window.onload = inactivityTime();
