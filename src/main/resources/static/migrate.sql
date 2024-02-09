alter table task drop column due_date_week_number;
alter table task drop column done;
alter table task add column done_at DATETIME default null;
alter table task modify column left_behind DATETIME default null;
update task set task.done_at = created_at;
