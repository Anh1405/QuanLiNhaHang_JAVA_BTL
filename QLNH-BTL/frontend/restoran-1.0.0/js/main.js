(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();
    
    
    // Initiate the wowjs
    new WOW().init();


    // Sticky Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 45) {
            $('.navbar').addClass('sticky-top shadow-sm');
        } else {
            $('.navbar').removeClass('sticky-top shadow-sm');
        }
    });
    
    
    // Dropdown on mouse hover
    const $dropdown = $(".dropdown");
    const $dropdownToggle = $(".dropdown-toggle");
    const $dropdownMenu = $(".dropdown-menu");
    const showClass = "show";
    
    $(window).on("load resize", function() {
        if (this.matchMedia("(min-width: 992px)").matches) {
            $dropdown.hover(
            function() {
                const $this = $(this);
                $this.addClass(showClass);
                $this.find($dropdownToggle).attr("aria-expanded", "true");
                $this.find($dropdownMenu).addClass(showClass);
            },
            function() {
                const $this = $(this);
                $this.removeClass(showClass);
                $this.find($dropdownToggle).attr("aria-expanded", "false");
                $this.find($dropdownMenu).removeClass(showClass);
            }
            );
        } else {
            $dropdown.off("mouseenter mouseleave");
        }
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Facts counter
    $('[data-toggle="counter-up"]').counterUp({
        delay: 10,
        time: 2000
    });


    // Modal Video
    $(document).ready(function () {
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
        console.log($videoSrc);

        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        center: true,
        margin: 24,
        dots: true,
        loop: true,
        nav : false,
        responsive: {
            0:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            }
        }
    });
    // --- LOGIC KIỂM TRA ĐĂNG NHẬP DÙNG CHUNG ---

})(jQuery);
window.loadAuthArea = function() {
    const authArea = document.getElementById("auth-area");
    if (!authArea) return;

    const userJson = localStorage.getItem("user");
    const currentLang = localStorage.getItem("currentLanguage") || "vi";

    if (userJson) {
        const user = JSON.parse(userJson);
        // Ưu tiên hiển thị hoTen (hoặc hoTen), nếu không có thì hiện username
        const tenHienThi = user.hoTen || user.username || "User";

        // Định nghĩa bộ từ vựng dịch nhanh cho các nút bấm trong thẻ Dropdown người dùng
        const authLabels = {
            profile: currentLang === "en" ? "Personal Profile" : "Thông tin cá nhân",
            orders: currentLang === "en" ? "My Orders" : "Món đã đặt",
            logout: currentLang === "en" ? "Log Out" : "Đăng xuất"
        };

        authArea.innerHTML = `
            <div class="nav-item dropdown">
                <a href="#" class="nav-link dropdown-toggle text-primary fw-bold" data-bs-toggle="dropdown">
                    <i class="fa fa-user-circle me-1"></i>${tenHienThi}
                </a>
                <div class="dropdown-menu m-0 shadow-sm border-0">
                    <a href="profile.html" class="dropdown-item">
                        <i class="fa fa-id-card me-2"></i>${authLabels.profile}
                    </a>
                    <a href="order_details.html" class="dropdown-item">
                        <i class="fa fa-utensils me-2"></i>${authLabels.orders}
                    </a>
                    <div class="dropdown-divider"></div>
                    <a href="#" class="dropdown-item text-danger" onclick="handleLogout(event)">
                        <i class="fa fa-sign-out-alt me-2"></i>${authLabels.logout}
                    </a>
                </div>
            </div>
        `;
    } else {
        // Trường hợp chưa đăng nhập: Trả về nút Đăng nhập tĩnh có gắn thẻ data-i18n chuẩn
        authArea.innerHTML = `
            <a href="login.html" class="nav-link" data-i18n="nav-login">
                <i class="fa fa-user me-1"></i>Đăng nhập
            </a>
        `;
        
        // Nếu file language.js đã load xong, ép nó dịch lại ngay nhãn "Đăng nhập" tĩnh vừa chèn
        if (typeof applyLanguage === "function") {
            const savedLang = localStorage.getItem("currentLanguage") || "vi";
            // Chỉ chạy quét lại các thẻ tĩnh chứ không gọi đệ quy ngược lại hàm loadAuthArea
            document.querySelectorAll("#auth-area [data-i18n]").forEach(element => {
                const key = element.getAttribute("data-i18n");
                if (dictionary[savedLang] && dictionary[savedLang][key]) {
                    element.textContent = dictionary[savedLang][key];
                }
            });
        }
    }
};

// Khởi chạy đồng bộ ngay khi cấu trúc cây DOM được thiết lập
document.addEventListener("DOMContentLoaded", function () {
    window.loadAuthArea();
});

// Hàm xử lý đăng xuất mẫu đồng bộ hệ thống
window.handleLogout = function(event) {
    if(event) event.preventDefault();
    localStorage.removeItem("user");
    window.location.href = "index.html";
};

// Hàm đăng xuất dùng chung cho mọi trang
function handleLogout() {
    if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
        localStorage.removeItem("user");
        // Sau khi xóa xong, tải lại trang để Navbar cập nhật lại chữ "Đăng Nhập"
        window.location.href = "index.html"; 
    }
}

