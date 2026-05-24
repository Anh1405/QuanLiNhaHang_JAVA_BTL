async function changePassword(event){

event.preventDefault();

const user = JSON.parse(
localStorage.getItem("user")
);

const oldPassword =
document.getElementById(
"oldPassword"
).value;

const newPassword =
document.getElementById(
"newPassword"
).value;

const confirmPassword =
document.getElementById(
"confirmPassword"
).value;


if(oldPassword !== user.password){

alert("Mật khẩu hiện tại không đúng");

return;
}

if(newPassword !== confirmPassword){

alert("Xác nhận mật khẩu không khớp");

return;
}

try{

const response = await axios.put(

`http://localhost:8080/api/nguoidung/${user.id}/matkhau`,

{
password:newPassword
}

);

console.log(response);

user.password=newPassword;

localStorage.setItem(
"user",
JSON.stringify(user)
);

alert("Đổi mật khẩu thành công");

window.location.href=
"profile.html";

}
catch(error){

console.log(error);

alert(
"Không thể đổi mật khẩu"
);

}

}