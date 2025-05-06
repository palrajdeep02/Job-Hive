// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Add smooth scrolling to all links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });

    // Mobile menu toggle functionality
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const navLinks = document.querySelector('.nav-links');
    
    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener('click', () => {
            navLinks.classList.toggle('active');
        });
    }

    // Login form handling
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            if (!this.checkValidity()) {
                e.stopPropagation();
                this.classList.add('was-validated');
                return;
            }

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password })
                });

                if (response.ok) {
                    const data = await response.json();
                    // Store the JWT token
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('role', data.role);
                    
                    // Redirect based on role
                    switch(data.role) {
                        case 'ADMIN':
                            window.location.href = '/admin/dashboard.html';
                            break;
                        case 'EMPLOYER':
                            window.location.href = '/employer/dashboard.html';
                            break;
                        case 'JOB_SEEKER':
                            window.location.href = '/jobseeker/dashboard.html';
                            break;
                        default:
                            window.location.href = '/';
                    }
                } else {
                    const error = await response.json();
                    alert(error.message || 'Login failed. Please check your credentials.');
                }
            } catch (error) {
                console.error('Login error:', error);
                alert('An error occurred during login. Please try again.');
            }
        });
    }

    // Registration form handling
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            if (!this.checkValidity()) {
                e.stopPropagation();
                this.classList.add('was-validated');
                return;
            }

            const name = document.getElementById('fullName').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const role = document.querySelector('input[name="role"]:checked').value.toUpperCase();

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ 
                        name,
                        email,
                        password,
                        role
                    })
                });

                if (response.ok) {
                    const data = await response.json();
                    // Store the JWT token
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('role', role);
                    
                    // Redirect based on role
                    switch(role) {
                        case 'EMPLOYER':
                            window.location.href = '/employer/dashboard.html';
                            break;
                        case 'JOB_SEEKER':
                            window.location.href = '/jobseeker/dashboard.html';
                            break;
                        default:
                            window.location.href = '/';
                    }
                } else {
                    const error = await response.json();
                    alert(error.message || 'Registration failed. Please try again.');
                }
            } catch (error) {
                console.error('Registration error:', error);
                alert('An error occurred during registration. Please try again.');
            }
        });
    }

    // Job search functionality
    const searchForm = document.querySelector('.job-search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const searchTerm = this.querySelector('input[type="search"]').value;
            const location = this.querySelector('input[type="text"]').value;
            // Implement job search logic here
            console.log('Searching for:', searchTerm, 'in', location);
        });
    }

    // Job filter functionality
    const filterButtons = document.querySelectorAll('.filter-btn');
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            const filter = this.dataset.filter;
            // Implement filtering logic here
            console.log('Filtering by:', filter);
        });
    });

    // Initialize tooltips if Bootstrap is used
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }

    // Validate session on page load
    validateSession();
    
    // Add event listener for logout button
    const logoutBtn = document.querySelector('a[href="../login.html"]');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            logout();
        });
    }

    // Add event listener for all navigation links
    document.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', async function(e) {
            // Skip logout link and external links
            if (this.href.includes('login.html') || this.href.startsWith('http')) {
                return;
            }
            
            // For internal links, validate session before navigation
            if (this.href.startsWith(window.location.origin)) {
                e.preventDefault();
                const isValid = await validateSession();
                if (isValid) {
                    window.location.href = this.href;
                }
            }
        });
    });
});

// Enhanced login function
async function login(email, password) {
    try {
        console.log('Login attempt initiated for email:', email);
        
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Login successful:', {
                user: data.user,
                role: data.role,
                timestamp: new Date().toISOString()
            });
            
            // Store user information
            localStorage.setItem('userRole', data.role);
            localStorage.setItem('userEmail', data.user.email);
            localStorage.setItem('lastLogin', new Date().toISOString());
            
            // Redirect based on role
            switch(data.role) {
                case 'ADMIN':
                    window.location.href = '/admin/dashboard.html';
                    break;
                case 'JOB_SEEKER':
                    window.location.href = '/jobseeker/dashboard.html';
                    break;
                case 'EMPLOYER':
                    window.location.href = '/employer/dashboard.html';
                    break;
                default:
                    console.error('Unknown role:', data.role);
                    showError('Invalid role received');
            }
        } else {
            const error = await response.text();
            console.error('Login failed:', {
                status: response.status,
                error: error,
                timestamp: new Date().toISOString()
            });
            showError(error);
        }
    } catch (error) {
        console.error('Login error:', {
            error: error.message,
            timestamp: new Date().toISOString()
        });
        showError('An error occurred during login');
    }
}

// Enhanced logout function
async function logout() {
    try {
        const userEmail = localStorage.getItem('userEmail');
        const userRole = localStorage.getItem('userRole');
        const lastLogin = localStorage.getItem('lastLogin');
        const timestamp = new Date().toISOString();
        
        console.log('Logout initiated:', {
            userEmail: userEmail,
            userRole: userRole,
            lastLogin: lastLogin,
            timestamp: timestamp,
            path: window.location.pathname
        });
        
        // Clear all stored data
        localStorage.clear();
        sessionStorage.clear();
        
        // Remove any cookies
        document.cookie.split(";").forEach(function(c) { 
            document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); 
        });
        
        // Send logout request
        const response = await fetch('/api/auth/logout', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (response.ok) {
            console.log('Logout successful:', {
                userEmail: userEmail,
                userRole: userRole,
                timestamp: timestamp,
                path: window.location.pathname
            });
            window.location.replace('/login.html?logout=success');
        } else {
            console.error('Logout failed:', {
                status: response.status,
                userEmail: userEmail,
                userRole: userRole,
                timestamp: timestamp,
                path: window.location.pathname
            });
            window.location.replace('/login.html?logout=error');
        }
    } catch (error) {
        console.error('Logout error:', {
            error: error.message,
            timestamp: new Date().toISOString(),
            path: window.location.pathname
        });
        window.location.replace('/login.html?logout=error');
    }
}

// Enhanced session validation
async function validateSession() {
    try {
        const path = window.location.pathname;
        const userEmail = localStorage.getItem('userEmail');
        const userRole = localStorage.getItem('userRole');
        
        console.log('Session validation started:', {
            path: path,
            userEmail: userEmail,
            userRole: userRole,
            timestamp: new Date().toISOString()
        });
        
        // Check if we're on a protected page
        if (path.startsWith('/jobseeker/') || path.startsWith('/employer/') || path.startsWith('/admin/')) {
            // First check if we have any stored credentials
            if (!userEmail || !userRole) {
                console.log('No stored credentials found, redirecting to login');
                window.location.replace('/login.html?session=expired');
                return false;
            }
            
            const response = await fetch('/api/auth/validate', {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!response.ok) {
                console.log('Session validation failed:', {
                    status: response.status,
                    path: path,
                    userEmail: userEmail,
                    userRole: userRole,
                    timestamp: new Date().toISOString()
                });
                
                if (response.status === 401) {
                    localStorage.clear();
                    sessionStorage.clear();
                    window.location.replace('/login.html?session=expired');
                    return false;
                }
                throw new Error('Session validation failed');
            }
            
            const data = await response.json();
            console.log('Session validation successful:', {
                path: path,
                userEmail: userEmail,
                userRole: userRole,
                serverData: data,
                timestamp: new Date().toISOString()
            });
            
            return true;
        }
        return true;
    } catch (error) {
        console.error('Session validation error:', {
            error: error.message,
            path: window.location.pathname,
            timestamp: new Date().toISOString()
        });
        return false;
    }
} 