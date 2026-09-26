


IF DB_ID('JavaAdv') IS NULL
    CREATE DATABASE JavaAdv;
GO

USE JavaAdv;
GO

IF OBJECT_ID('dbo.Upis', 'U') IS NOT NULL DROP TABLE dbo.Upis;
IF OBJECT_ID('dbo.Polaznik', 'U') IS NOT NULL DROP TABLE dbo.Polaznik;
IF OBJECT_ID('dbo.ProgramObrazovanja', 'U') IS NOT NULL DROP TABLE dbo.ProgramObrazovanja;
GO

CREATE TABLE dbo.Polaznik (
    PolaznikID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Ime NVARCHAR(100) NOT NULL,
    Prezime NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE dbo.ProgramObrazovanja (
    ProgramObrazovanjaID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Naziv NVARCHAR(100) NOT NULL,
    CSVET INT NOT NULL
);
GO

CREATE TABLE dbo.Upis (
    UpisID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    IDPolaznik INT NOT NULL,
    IDProgramObrazovanja INT NOT NULL,
    CONSTRAINT FK_Upis_Polaznik FOREIGN KEY (IDPolaznik)
        REFERENCES dbo.Polaznik(PolaznikID),
    CONSTRAINT FK_Upis_Program FOREIGN KEY (IDProgramObrazovanja)
        REFERENCES dbo.ProgramObrazovanja(ProgramObrazovanjaID),
    CONSTRAINT UQ_Upis_Polaznik UNIQUE (IDPolaznik)
);
GO

-- 1. Unos polaznika
CREATE OR ALTER PROCEDURE dbo.sp_UnesiPolaznika
    @Ime NVARCHAR(100),
    @Prezime NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.Polaznik (Ime, Prezime)
    VALUES (@Ime, @Prezime);
    SELECT CAST(SCOPE_IDENTITY() AS INT) AS PolaznikID;
END;
GO

-- 2. Unos programa obrazovanja
CREATE OR ALTER PROCEDURE dbo.sp_UnesiProgram
    @Naziv NVARCHAR(100),
    @CSVET INT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.ProgramObrazovanja (Naziv, CSVET)
    VALUES (@Naziv, @CSVET);
    SELECT CAST(SCOPE_IDENTITY() AS INT) AS ProgramObrazovanjaID;
END;
GO

-- 3. Upis polaznika na program
CREATE OR ALTER PROCEDURE dbo.sp_UpisiPolaznika
    @IDPolaznik INT,
    @IDProgramObrazovanja INT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.Upis (IDPolaznik, IDProgramObrazovanja)
    VALUES (@IDPolaznik, @IDProgramObrazovanja);
END;
GO

-- 4. Prebacivanje: namjerno je odvojeno brisanje i novi upis
-- kako bi Java aplikacija mogla demonstrirati transakciju.
CREATE OR ALTER PROCEDURE dbo.sp_ObrisiUpis
    @IDPolaznik INT
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM dbo.Upis WHERE IDPolaznik = @IDPolaznik;
END;
GO

CREATE OR ALTER PROCEDURE dbo.sp_DodajUpis
    @IDPolaznik INT,
    @IDProgramObrazovanja INT
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.Upis (IDPolaznik, IDProgramObrazovanja)
    VALUES (@IDPolaznik, @IDProgramObrazovanja);
END;
GO

-- 5. Ispis polaznika za zadani program
CREATE OR ALTER PROCEDURE dbo.sp_PolazniciPrograma
    @IDProgramObrazovanja INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT
        p.Ime,
        p.Prezime,
        pr.Naziv,
        pr.CSVET
    FROM dbo.Upis u
    INNER JOIN dbo.Polaznik p ON p.PolaznikID = u.IDPolaznik
    INNER JOIN dbo.ProgramObrazovanja pr
        ON pr.ProgramObrazovanjaID = u.IDProgramObrazovanja
    WHERE pr.ProgramObrazovanjaID = @IDProgramObrazovanja
    ORDER BY p.Prezime, p.Ime;
END;
GO

GO
CREATE OR ALTER PROCEDURE dbo.sp_DohvatiPolaznika
    @PolaznikID INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT PolaznikID, Ime, Prezime
    FROM dbo.Polaznik
    WHERE PolaznikID = @PolaznikID;
END;
GO

CREATE OR ALTER PROCEDURE dbo.sp_DohvatiProgram
    @ProgramObrazovanjaID INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT ProgramObrazovanjaID, Naziv, CSVET
    FROM dbo.ProgramObrazovanja
    WHERE ProgramObrazovanjaID = @ProgramObrazovanjaID;
END;
GO

GO
CREATE OR ALTER PROCEDURE dbo.sp_DohvatiUpisPolaznika
    @IDPolaznik INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT UpisID, IDPolaznik, IDProgramObrazovanja
    FROM dbo.Upis
    WHERE IDPolaznik = @IDPolaznik;
END;
GO
