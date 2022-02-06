drop procedure delete_user;
create procedure delete_user(IN userid bigint)
BEGIN

delete
from t_security_group_users
where id_user = userid;
delete
from avogest.t_dossier_rights
where VC_USER_ID in (
    select id
    from t_virtualcab_users
    where id_user = userid);
delete
from avogest.t_virtualcab_users
where id_user = userid;
delete
from t_stripe_subscribers
where id_user = userId;
delete
from t_timesheet
where id_gest = userid;
delete
from t_obj_shared_with
where to_userid = userid;
delete
from t_obj_shared_with
where from_userid = userid;
delete
from t_users
where id = userid;
delete
from t_first_time
where USER_ID = userid;
END;

