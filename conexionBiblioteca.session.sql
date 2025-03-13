

ALTER TABLE editorial MODIFY COLUMN id VARCHAR(36);


ALTER TABLE libro DROP FOREIGN KEY FK79q7g2604hcmfdxw6ek3jt4el ;
ALTER TABLE editorial MODIFY COLUMN id VARCHAR(36);
ALTER TABLE libro MODIFY COLUMN editorial_id VARCHAR(36);
ALTER TABLE libro 
ADD CONSTRAINT FK79q7g2604hcmfdxw6ek3jt4el
FOREIGN KEY (editorial_id) 
REFERENCES editorial(id);

show create table editorial;
show  create table libro;
select * from editorial;