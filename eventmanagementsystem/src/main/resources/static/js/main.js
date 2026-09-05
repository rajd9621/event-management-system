/* ============================================
   Smart Event Management System - JavaScript
   ============================================ */

document.addEventListener('DOMContentLoaded', function () {

    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            if (alert.classList.contains('alert-dismissible')) {
                const closeBtn = alert.querySelector('.btn-close');
                if (closeBtn) closeBtn.click();
                else {
                    alert.style.transition = 'opacity 0.5s';
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 500);
                }
            }
        }, 5000);
    });

    // Confirm destructive actions
    document.querySelectorAll('a[onclick*="confirm"]').forEach(function (link) {
        link.addEventListener('click', function (e) {
            const msg = link.getAttribute('onclick');
            if (msg && msg.includes('confirm')) {
                // The inline onclick handles confirmation
            }
        });
    });

    // Add active class to current nav link
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar-nav .nav-link').forEach(function (link) {
        const href = link.getAttribute('href');
        if (href && (href === currentPath || (href !== '/home' && currentPath.startsWith(href)))) {
            link.classList.add('active');
        }
    });

    // Flash message auto-hide (URL params)
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error') || urlParams.has('registered') || urlParams.has('logout')) {
        // Clean URL after showing message
        setTimeout(function () {
            if (window.history && window.history.replaceState) {
                window.history.replaceState({}, document.title, window.location.pathname);
            }
        }, 6000);
    }

    console.log('EventHub application loaded successfully.');
});
