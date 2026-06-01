document.addEventListener(
"DOMContentLoaded",
function(){

const user=
JSON.parse(
localStorage.getItem("user")
);

document.getElementById(
"hoTen"
).value=user.hoTen;

document.getElementById(
"email"
).value=user.email;

document.getElementById(
"username"
).value=user.username;

}
);


async function updateProfile(event){
    const email = document.getElementById("email").value.trim();

    const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;

    if (!emailRegex.test(email)) {
        alert("Chỉ được dùng email @gmail.com");
        document.getElementById("email").focus();
        return;
    }

event.preventDefault();

const user=
JSON.parse(
localStorage.getItem("user")
);

try{

const response=
await axios.put(

`http://localhost:8080/api/nguoidung/${user.id}/capnhat`,

{
hoTen:
document.getElementById(
"hoTen"
).value,

email:
document.getElementById(
"email"
).value,

username:
document.getElementById(
"username"
).value,

currentPassword:
document.getElementById(
"currentPassword"
).value
}

);

localStorage.setItem(
"user",
JSON.stringify(
response.data
)
);

alert(
"Cập nhật thành công"
);

window.location.href=
"profile.html";

}
catch{

alert(
"Không thể cập nhật"
);

}

}