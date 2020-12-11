<img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/logo.jpg width = 75%">

<p>This application is using a free api (https://ratpark-api.imok.space/) for getting restaurant data with <b>Retrofit</b>, and it will
save it to a local database (implemented with <b>Room</b>).</p>
<p>
The application contains a splash screen. This is where the application will decide from where the necessary data should be
loaded (default city restaurants) and will check the <b>SharedPreferences</b> for login credentials. If there are credentials and
they are correct, it will automatically navigate to the main screen.
</p>
<p float="left">
    <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/splash.jpg" width="24%" />
    <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/main.jpg" width="24%" />
</p>
<p>
If the credentials do not exist or they are wrong, the application will navigate the user to the register screen.
The user also can login if he/she already has an account.
</p>
<p float="left">
    <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/register.jpg" width="24%" />
    <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/login.jpg" width="24%" />
</p>

<p>
The application contains a <b>bottom navigation menu</b> with three destination: <b>Home</b>, <b>Favorites</b>, <b>Profile</b>.
</p>

<h3>Home Screen</h3>
<p float="left">
  <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/main.jpg" width="24%" />
  <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/filter.jpg" width="24%" />
</p>
<p>Here we can watch the list of the restaurants (implemented with <b>RecycleView</b>) with some
details, <b>add to favorites</b>, <b>filter</b> and <b>search</b> among them (with a <b>custom toolbar</b>). When we
click on an item, the application will navigate to the detail screen.</p>

<h3> Detail screen </h3>
 <img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/detail.jpg" width="24%" />
<p> On this screen we can observe more details about the selected restaurant, we can add or delete images
(which appear in a <b>Horizontal RecycleView</b>), open the coordinates on <b>Google Maps</b> or
<b>Call</b> the selected restaurant. When we upload a picture, the application will <b>resize</b> it with keeping the
pixel ratio, for the better performance and will store as ByteArray in the local database.
</p>

<h3>Favorites screen</h3>
<img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/favorite.jpg" width="24%" />
<p> On the favorites screen the user can delete or open a restaurant which was added to the favorites.</p>

<h3> Profile fragment </h3>
<img src="https://github.com/RockieHUN/RestaurantsApplication/blob/master/images/profile.jpg" width="24%" />
<p>Just a fancy profile screen where the user can <b>logout</b> or <b>change the profile picture</b>. </p>

<h3> Summary </h3>

<p>Components used in the project:
<lu>
<li> Activities (MainActivity, GoogleMaps, Call, ImageSelection)</li>
<li> Fragments (a lot)</li>
<li> Retrofit</li>
<li> Room </li>
<li> Coroutines </li>
<li> Custom toolbar </li>
<li> Bottom Navigation Menu </li>
<li> Recycle View</li>
<li> Dialog box </li>
<li> LiveData and Observers</li>
<li> Shared Preferences</li>
<li> etc.</li>
</lu>
<br>

Made for a university project. <br>
Last commit: <b> 11.12.2020 </b>.

</p>


