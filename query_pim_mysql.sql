-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema foxdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema foxdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `foxdb` DEFAULT CHARACTER SET utf8 ;
USE `foxdb` ;

-- -----------------------------------------------------
-- Table `foxdb`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `senha` VARCHAR(45) NOT NULL,
  `codigo` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foxdb`.`funcionarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`funcionarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `documento` VARCHAR(15) NOT NULL,
  `email` VARCHAR(25) NOT NULL,
  `funcao` VARCHAR(15) NOT NULL,
  `data_admissao` DATE NOT NULL,
  `telefone` VARCHAR(15) NOT NULL,
  `endereco` VARCHAR(100) NOT NULL,
  `cidade` VARCHAR(15) NOT NULL,
  `estado` VARCHAR(2) NOT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_funcionarios_1_idx` (`id_usuario` ASC) ,
  CONSTRAINT `fk_funcionarios_1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `foxdb`.`usuarios` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foxdb`.`clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`clientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `documento` VARCHAR(20) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `data_nascimento` DATE NOT NULL,
  `telefone` VARCHAR(15) NOT NULL,
  `endereco` VARCHAR(100) NOT NULL,
  `cidade` VARCHAR(25) NOT NULL,
  `estado` VARCHAR(2) NOT NULL,
  `status` INT NOT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_clientes_1_idx` (`id_usuario` ASC) ,
  CONSTRAINT `fk_clientes_1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `foxdb`.`usuarios` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foxdb`.`carteiras_digitais`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`carteiras_digitais` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(40) NOT NULL,
  `id_cliente` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_carteiras_digitais_1_idx` (`id_cliente` ASC),
  CONSTRAINT `fk_carteiras_digitais_1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `foxdb`.`clientes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foxdb`.`criptomoedas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`criptomoedas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foxdb`.`transacoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`transacoes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `data_hora` DATETIME NOT NULL,
  `valor` DECIMAL(8,2) NOT NULL,
  `id_carteira_digital` INT NOT NULL,
  `id_criptomoeda` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transacoes_1_idx` (`id_carteira_digital` ASC),
  INDEX `fk_transacoes_2_idx` (`id_criptomoeda` ASC),
  CONSTRAINT `fk_transacoes_1`
    FOREIGN KEY (`id_carteira_digital`)
    REFERENCES `foxdb`.`carteiras_digitais` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transacoes_2`
    FOREIGN KEY (`id_criptomoeda`)
    REFERENCES `foxdb`.`criptomoedas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foxdb`.`administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foxdb`.`administradores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_funcionario` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_administradores_1_idx` (`id_funcionario` ASC),
  CONSTRAINT `fk_administradores_1`
    FOREIGN KEY (`id_funcionario`)
    REFERENCES `foxdb`.`funcionarios` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
