(function(window, up){
  window.onSignIn = function (googleUser) {
    var idToken = googleUser.getAuthResponse().id_token;
    var profile = googleUser.getBasicProfile();

    up.request("/users/login", {
      method: "POST",
      params: { idToken: idToken }
    }).then(function(response) {
      window.location.replace(response.url);
    }).catch(function(response) {
      window.location.replace("/oops");
    });
  }
})(window, up);
