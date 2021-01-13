create table RegionLocalization (
	mvccVersion LONG default 0 not null,
	regionLocalizationId LONG not null primary key,
	companyId LONG,
	regionId LONG,
	languageId VARCHAR(75) null,
	title VARCHAR(75) null
);

create unique index IX_A149763D on RegionLocalization (regionId, languageId[$COLUMN_LENGTH:75$]);

COMMIT_TRANSACTION;