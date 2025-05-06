function logout() {
    // Prevent multiple clicks
    const logoutBtn = document.querySelector('a[onclick="logout(); return false;"]');
    if (logoutBtn) {
        logoutBtn.style.pointerEvents = 'none';
        logoutBtn.style.opacity = '0.7';
    }

    // Clear any client-side storage
    localStorage.clear();
    sessionStorage.clear();
    
    // Call the logout endpoint
    fetch('/api/auth/logout', {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            // Redirect to login page with success message
            window.location.href = '/login.html?logout=success';
        } else {
            console.error('Logout failed:', response.status);
            // Still redirect to login page
            window.location.href = '/login.html';
        }
    })
    .catch(error => {
        console.error('Logout error:', error);
        // Still redirect to login page even if there's an error
        window.location.href = '/login.html';
    })
    .finally(() => {
        // Re-enable the button in case of error
        if (logoutBtn) {
            logoutBtn.style.pointerEvents = 'auto';
            logoutBtn.style.opacity = '1';
        }
    });
} 