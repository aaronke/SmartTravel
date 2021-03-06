// Code generated by dagger-compiler.  Do not edit.
package com.aaron.smarttravel.injection;

import dagger.internal.BindingsGroup;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import javax.inject.Provider;

/**
 * A manager of modules and provides adapters allowing for proper linking and
 * instance provision of types served by {@code @Provides} methods.
 */
public final class GeneralModule$$ModuleAdapter extends ModuleAdapter<GeneralModule> {
  private static final String[] INJECTS = { "members/com.aaron.smarttravel.injection.SmartTravelApplication", "members/com.aaron.smarttravel.main.MainActivity", "members/com.aaron.smarttravelbackground.WarningService", "members/com.aaron.smarttravel.main.MapFragment", "members/com.aaron.smarttravel.main.HomeActivity", "members/com.aaron.smarttravel.main.SampleListFragmentLeft", };
  private static final Class<?>[] STATIC_INJECTIONS = { };
  private static final Class<?>[] INCLUDES = { };

  public GeneralModule$$ModuleAdapter() {
    super(com.aaron.smarttravel.injection.GeneralModule.class, INJECTS, STATIC_INJECTIONS, false /*overrides*/, INCLUDES, false /*complete*/, true /*library*/);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getBindings(BindingsGroup bindings, GeneralModule module) {
    bindings.contributeProvidesBinding("android.content.Context", new ProvideApplicationContextProvidesAdapter(module));
    bindings.contributeProvidesBinding("com.squareup.otto.Bus", new ProvideBusProvidesAdapter(module));
  }

  /**
   * A {@code Binding<android.content.Context>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<android.content.Context>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideApplicationContextProvidesAdapter extends ProvidesBinding<android.content.Context>
      implements Provider<android.content.Context> {
    private final GeneralModule module;

    public ProvideApplicationContextProvidesAdapter(GeneralModule module) {
      super("android.content.Context", IS_SINGLETON, "com.aaron.smarttravel.injection.GeneralModule", "provideApplicationContext");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<android.content.Context>}.
     */
    @Override
    public android.content.Context get() {
      return module.provideApplicationContext();
    }
  }

  /**
   * A {@code Binding<com.squareup.otto.Bus>} implementation which satisfies
   * Dagger's infrastructure requirements including:
   *
   * Being a {@code Provider<com.squareup.otto.Bus>} and handling creation and
   * preparation of object instances.
   */
  public static final class ProvideBusProvidesAdapter extends ProvidesBinding<com.squareup.otto.Bus>
      implements Provider<com.squareup.otto.Bus> {
    private final GeneralModule module;

    public ProvideBusProvidesAdapter(GeneralModule module) {
      super("com.squareup.otto.Bus", IS_SINGLETON, "com.aaron.smarttravel.injection.GeneralModule", "provideBus");
      this.module = module;
      setLibrary(true);
    }

    /**
     * Returns the fully provisioned instance satisfying the contract for
     * {@code Provider<com.squareup.otto.Bus>}.
     */
    @Override
    public com.squareup.otto.Bus get() {
      return module.provideBus();
    }
  }
}
