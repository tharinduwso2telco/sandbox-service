import { SandboxUiPage } from './app.po';

describe('sandbox-ui App', function() {
  let page: SandboxUiPage;

  beforeEach(() => {
    page = new SandboxUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
