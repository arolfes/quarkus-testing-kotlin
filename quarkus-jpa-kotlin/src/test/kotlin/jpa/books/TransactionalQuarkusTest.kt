package jpa.books

import io.quarkus.test.junit.QuarkusTest;

import javax.enterprise.inject.Stereotype;
import javax.transaction.Transactional;
import java.lang.annotation.ElementType;

@QuarkusTest
@Stereotype
@Transactional
@Retention
@Target(AnnotationTarget.CLASS)
annotation class TransactionalQuarkusTest