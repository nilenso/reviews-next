(function(){
  window.onSignIn = function (googleUser) {
    var id_token = googleUser.getAuthResponse().id_token;
    var profile = googleUser.getBasicProfile();

    up.request("/users/login/", {
      method: "POST",
      params: {
        user: {
          id: profile.getId(),
          name: profile.getName(),
          imageurl: profile.getImageUrl(),
          email: profile.getEmail()
        }, 
        id_token: id_token
      }
    }).then(function(response) {
      window.location.replace(response.url);
    });
  }
})();
