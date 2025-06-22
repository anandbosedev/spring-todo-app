CREATE USER todo_dev_auth WITH PASSWORD 'todo_dev_auth_password';
CREATE DATABASE todo_dev_auth OWNER todo_dev_auth;

CREATE USER todo_prod_auth WITH PASSWORD 'todo_prod_auth_password';
CREATE DATABASE todo_prod_auth OWNER todo_prod_auth;

CREATE USER todo_resource_dev WITH PASSWORD 'todo_resource_dev_password';
CREATE DATABASE todo_resource_dev OWNER todo_resource_dev;

CREATE USER todo_resource_prod WITH PASSWORD 'todo_resource_prod_password';
CREATE DATABASE todo_resource_prod OWNER todo_resource_prod;