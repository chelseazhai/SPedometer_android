CREATE TABLE "sp_personal_walkrecord" ("_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "userId" INTEGER NOT NULL, "startTimestamp" TIMESTAMP NOT NULL, "durationTime" INTEGER NOT NULL DEFAULT 0, "totalStep" INTEGER NOT NULL DEFAULT 0, "totalDistance" DOUBLE NOT NULL DEFAULT 0.0, "energy" FLOAT NOT NULL DEFAULT 0.0)