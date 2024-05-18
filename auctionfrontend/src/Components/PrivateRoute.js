export { PrivateRoute };

function PrivateRoute({ component: Component, alowedRoles }) {
   const roles=JSON.parse(localStorage.getItem("roles"));
    console.log(alowedRoles);
   if (!alowedRoles.some(role=>roles.includes(role))) {
      return <div>UNAUTHORIZED!!!!!!</div>;
    }
  
    return <Component/>;
}