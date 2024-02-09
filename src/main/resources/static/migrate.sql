alter table task drop column due_date_week_number;
alter table task add column done_at DATETIME default null;
update task set done_at = created_at WHERE done = true;
alter table task drop column done;
alter table task modify column left_behind DATETIME default null;
