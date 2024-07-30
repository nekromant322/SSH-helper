INSERT INTO public.t_role(id, role)
VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
INSERT INTO public.t_user(id, name, password)
VALUES (1, 'ilai_s', '$2y$10$O0vKt3oXzrPvU6bup5VBoe6.1rzEtu6nr5DEsQ7nXNNsPLfGcLH86');
INSERT INTO public.t_user_roles(user_id, roles_id)
VALUES (1, 2);
