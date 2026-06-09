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
        const tenHienThi = user.username  || user.hoTen || "User";

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

window.handleLogout = function(event) {
    if(event) event.preventDefault(); 
    
    const currentLang = localStorage.getItem("currentLanguage") || "vi";
    const confirmMsg = currentLang === "en" ? "Are you sure you want to log out?" : "Bạn có chắc chắn muốn đăng xuất?";

    if (confirm(confirmMsg)) {
        localStorage.removeItem("user");
        window.location.href = "index.html"; 
    }
};
// 1. Hàm tính toán và cập nhật số lượng hiển thị trên Badge giỏ hàng
function formatTienMini(tien) {
    const giaTriSo = Number(tien) || 0;
    const currentLang = localStorage.getItem("currentLanguage") || "vi";
    if (currentLang === "en") {
        return (giaTriSo / 26270).toLocaleString('en-US', { style: 'currency', currency: 'USD' });
    }
    return giaTriSo.toLocaleString('vi-VN') + " VNĐ";
}

// Cập nhật số lượng Badge VÀ render danh sách món ăn vào Dropdown
window.capNhatBadgeGioHang = function(){
    const gioHang = JSON.parse(localStorage.getItem("gioHang")) || [];
    const badge = document.getElementById("cart-badge-count");
    const miniCart = document.getElementById("mini-cart-container");
    
    // 1. Lấy ngôn ngữ hiện tại
    const currentLang = localStorage.getItem("currentLanguage") || "vi";

    // 2. Bộ từ vựng dành riêng cho Mini Cart
    const i18nCart = {
        "empty": currentLang === "en" ? "Cart is empty!" : "Giỏ hàng trống trơn!",
        "selected": currentLang === "en" ? "Selected Dishes" : "Món ăn đã chọn",
        "total": currentLang === "en" ? "Total:" : "Tổng cộng:",
        "viewCart": currentLang === "en" ? "View Cart Details" : "Xem Chi Tiết Giỏ Hàng"
    };
    
    // 3. Xử lý nhảy con số trên Badge
    const tongSoLuong = gioHang.reduce((total, mon) => total + mon.soLuong, 0);
    if (badge) {
        if (tongSoLuong > 0) {
            badge.innerText = tongSoLuong;
            badge.style.display = "inline-block";
        } else {
            badge.style.display = "none";
        }
    }

    // 4. Xử lý hiển thị danh sách món bên trong Dropdown "inner"
    if (miniCart) {
        if (gioHang.length === 0) {
            // Thay thế chữ cứng bằng biến i18nCart.empty
            miniCart.innerHTML = `<div class="text-center py-3 text-muted">${i18nCart.empty}</div>`;
            return;
        }

        // Thay thế chữ cứng bằng biến i18nCart.selected
        let htmlMonAn = `<h6 class="fw-bold border-bottom pb-2 mb-2 text-dark">${i18nCart.selected}</h6>`;
        let tongTien = 0;

        gioHang.forEach(mon => {
            tongTien += mon.gia * mon.soLuong;
            
            htmlMonAn += `
                <div class="d-flex align-items-center justify-content-between mb-2 border-bottom pb-2">
                    <img src="${mon.hinhAnh}" style="width:45px; height:45px; object-fit:cover; border-radius:4px;">
                    <div class="ms-2 flex-grow-1" style="font-size: 13px;">
                        <span class="fw-bold d-block text-truncate text-dark" style="max-width: 130px;">${mon.tenMon}</span>
                        <span class="text-primary small">${formatTienMini(mon.gia)}</span>
                    </div>
                    <div class="d-flex align-items-center">
                        <button class="btn btn-sm btn-light p-1 px-2 border" onclick="giamSoLuong(${mon.idMonAn})">-</button>
                        <span class="mx-2 fw-bold text-dark" style="font-size: 13px;">${mon.soLuong}</span>
                        <button class="btn btn-sm btn-light p-1 px-2 border" onclick="tangSoLuong(${mon.idMonAn})">+</button>
                    </div>
                </div>
            `;
        });

        // Thay thế chữ Tổng cộng và Xem chi tiết giỏ hàng
        htmlMonAn += `
            <div class="d-flex justify-content-between align-items-center mt-3 pt-2 font-weight-bold">
                <span class="text-dark fw-bold" style="font-size:14px;">${i18nCart.total}</span>
                <span class="text-primary fw-bold" style="font-size:15px;">${formatTienMini(tongTien)}</span>
            </div>
            <a href="booking.html" class="btn btn-primary btn-sm w-100 mt-3 py-2 fw-bold">${i18nCart.viewCart}</a>
        `;

        miniCart.innerHTML = htmlMonAn;
    }
}

// Kích hoạt cập nhật thời gian thực
window.kichHoatCapNhatGioHang = function() {
    if (typeof window.capNhatBadgeGioHang === "function") {
        window.capNhatBadgeGioHang();
    } else {
        capNhatBadgeGioHang(); // Gọi dự phòng
    }
    window.dispatchEvent(new Event('cart-updated'));
};

// Khởi chạy hệ thống lắng nghe nhảy số liên tục
document.addEventListener('DOMContentLoaded', function () {
    capNhatBadgeGioHang();

    window.addEventListener('storage', function (e) {
        if (e.key === 'gioHang') capNhatBadgeGioHang();
    });

    window.addEventListener('cart-updated', capNhatBadgeGioHang);
});
window.tangSoLuong = function(idMonAn) {
    let gioHang = JSON.parse(localStorage.getItem("gioHang")) || [];
    const index = gioHang.findIndex(item => item.idMonAn === idMonAn);
    
    if (index !== -1) {
        gioHang[index].soLuong++;
        localStorage.setItem("gioHang", JSON.stringify(gioHang));
        kichHoatCapNhatGioHang(); // Vẽ lại giao diện
    }
};
window.giamSoLuong = function(idMonAn) {
    let gioHang = JSON.parse(localStorage.getItem("gioHang")) || [];
    const index = gioHang.findIndex(item => item.idMonAn === idMonAn);
    
    if (index !== -1) {
        if (gioHang[index].soLuong > 1) {
            gioHang[index].soLuong--;
        } else {
            // Nếu số lượng lùi về 0, xóa món khỏi giỏ hàng
            gioHang.splice(index, 1); 
        }
        localStorage.setItem("gioHang", JSON.stringify(gioHang));
        kichHoatCapNhatGioHang(); // Vẽ lại giao diện
    }
};